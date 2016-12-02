package com.simulation.terminal.communication;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;
import com.simulation.terminal.util.PacketProcessing;

public class MessageListener implements Runnable {
	public static Logger logger = Logger.getLogger(MessageListener.class);
	private static byte SIGN_7E = 0X7E;
	private static ByteBuffer buffer = ByteBuffer.allocate(128);
	@Override
	public void run(){
		try{
			while(true){
				Selector selector = CacheManager.getSelector();
				try {
					selector.select();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey selectionKey = iter.next();
					try {
						if (selectionKey.isValid() && selectionKey.isReadable()) {
							SocketChannel channel = (SocketChannel) selectionKey.channel();
							channel.read(buffer);
							buffer.flip();
							decodeMessage(channel, buffer);
							buffer.clear();
						}
						iter.remove();
					} catch (IOException e) {
						selectionKey.cancel();
						e.printStackTrace();
						logger.error("接收消息链路异常："+e.getMessage());
					}
				}
			}
		}catch(Throwable cause){
			cause.printStackTrace();
		}
	}
	
	public void decodeMessage(SocketChannel channel, ByteBuffer buffer){
		int limit = buffer.limit();
		byte[] currentBytes = new byte[limit];
		buffer.get(currentBytes, 0, limit);
		byte[] lastBytes = CacheManager.getMessageCache().get(channel.hashCode());
		byte[] bytes = null;
		if(lastBytes != null){
			bytes = ArraysUtils.arraycopy(lastBytes, currentBytes);
			CacheManager.setMessageCache(channel.hashCode(), new byte[0]);
		}else{
			bytes = currentBytes;
		}
		List<byte[]> packets = PacketProcessing.subpackage(bytes, SIGN_7E, SIGN_7E, 14);
		if (packets != null) {
			for (byte[] array : packets) {
				if (array[0] == SIGN_7E && array[array.length - 1] == SIGN_7E && array.length > 13) {
					dealPacket(array);
				} else {
					CacheManager.setMessageCache(channel.hashCode(), array);
				}
			}
		}
	}
	
	private void dealPacket(byte[] srcBytes){
		StatisticalData.receivePacketNumber++;
		StatisticalData.receiveTotalPacketNumber++;
		StatisticalData.downFlow = srcBytes.length/1024d;
		StatisticalData.downTotalFlow = srcBytes.length/1024d;
		Packet packet = new Packet();
		byte[] tempBytes = new byte[srcBytes.length - 2];
		System.arraycopy(srcBytes, 1, tempBytes, 0, tempBytes.length);
		byte[] bytes = PacketProcessing.unEscape(tempBytes, PacketProcessing.toEscapeByte, PacketProcessing.escapeByte);
		int checkCode = bytes[bytes.length - 1];
		int tempCode = PacketProcessing.checkPackage(bytes, 0, bytes.length - 2);
		if (checkCode != tempCode) {
			return;
		}
		byte[] messageId = ArraysUtils.subarrays(bytes, 0, 2);
		packet.setMessageId(Convert.byte2Int(messageId, 2));
		byte[] tid = ArraysUtils.subarrays(bytes, 4, 6);
		packet.setTerminalId(Long.parseLong(Convert.bytesToHexString(tid)));
		byte[] serial = ArraysUtils.subarrays(bytes, 10, 2);
		packet.setSerialNumber(Convert.byte2Int(serial, 2));
		packet.setMessageBody(ArraysUtils.subarrays(bytes, 12, bytes.length-13));
		logger.info(packet.getTerminalId() + " 收到消息 : " + Convert.bytesToHexString(srcBytes));
		CommandFactory.processer(packet);
	}
}
