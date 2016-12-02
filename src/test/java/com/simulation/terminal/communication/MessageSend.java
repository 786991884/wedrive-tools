package com.simulation.terminal.communication;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.entity.MessageInfo;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;
import com.simulation.terminal.util.PacketProcessing;

public class MessageSend {
	public static Logger logger = Logger.getLogger(MessageSend.class);
	public void write(TerminalStatus status, byte[] message, long terminalId){
		try {
			SocketChannel channel = status.getChannel();
			if(status.isChannelActive() && channel!=null && channel.isConnected()){
				logger.info(terminalId + " 发送消息 : " + Convert.bytesToHexString(message));
				channel.write(ByteBuffer.wrap(message));
				StatisticalData.sendPacketNumber++;
				StatisticalData.sendTotalPacketNumber++;
				StatisticalData.upFlow += message.length/1024d;
				StatisticalData.upTotalFlow += message.length/1024d;
			}
		} catch (IOException e) {
			status.setChannelActive(false);
			logger.error("发送消息链路异常：" + e.getMessage());
			e.printStackTrace();
		}catch(Throwable cause){
			cause.printStackTrace();
		}
	}
	
	public void builder(Packet packet){
		byte[] message = null;
		byte[] body = packet.getMessageBody();
		byte[] data = new byte[13+body.length];
		ArraysUtils.arrayappend(data, 0, Convert.intTobytes(packet.getMessageId(), 2));
		ArraysUtils.arrayappend(data, 2, Convert.intTobytes(body.length, 2));
		ArraysUtils.arrayappend(data, 4, Convert.hexStringToBytes(Convert.preFillZero(packet.getTerminalId(), 12)));
		ArraysUtils.arrayappend(data, 10, Convert.intTobytes(packet.getSerialNumber(), 2));
		ArraysUtils.arrayappend(data, 12, body);
		byte checkCode = PacketProcessing.checkPackage(data, 0, data.length-1);
		data[data.length-1] = checkCode;
		byte[] bytes = PacketProcessing.escape(data, PacketProcessing.escapeByte, PacketProcessing.toEscapeByte);
		message = new byte[bytes.length+2];
		message[0] = 0x7E;
		ArraysUtils.arrayappend(message, 1, bytes);
		message[message.length-1] = 0x7E;
		MessageInfo info = new MessageInfo();
		info.setId(packet.getMessageId());
		info.setSerial(packet.getSerialNumber());
		info.setSendTime(new Date().getTime());
		CacheManager.setMessageCache(packet.getTerminalId(), info);
		TerminalStatus status = CacheManager.getTerminalCache().get(packet.getTerminalId());
		write(status, message, packet.getTerminalId());
	}
}
