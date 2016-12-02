package com.terminal.communication;

import com.terminal.cache.CacheManager;
import com.terminal.cache.CommandCache;
import com.terminal.entity.MessageInfo;
import com.terminal.entity.TerminalStatus;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import com.terminal.util.PacketProcessing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MessageSend {
    public static Logger logger = LoggerFactory.getLogger(MessageSend.class);

    public static AtomicLong totalSendBytes = new AtomicLong(0);

    public static AtomicLong sendTotalPacketNum = new AtomicLong(0);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CommandCache commandCache;


    public void write(TerminalStatus status, byte[] message, Packet packet) {
        try {
            Channel channel = status.getChannel();
            long terminalId = packet.getTerminalId();
            if (status.isChannelActive() && channel != null && channel.isActive()) {
                logger.info(terminalId + " 发送消息 : " + Convert.bytesToHexString(message));
                //String msg = Convert.bytesToHexString(message);
                //char[] chars = msg.toCharArray();
//                ByteBuf byteBuf = Unpooled.buffer();
                ByteBuf byteBuf = Unpooled.wrappedBuffer(message);
                /*for (int i = 0; i < chars.length; i++) {
                    if ((i != 0 && i % 2 != 0)) {
                        byteBuf.writeByte(Integer.valueOf(String.valueOf(chars[i - 1]) + String.valueOf(chars[i]), 16));
                    }

                }*/
                //final long sendBytes = byteBuf.readableBytes();
                ChannelFuture writefuture = channel.writeAndFlush(byteBuf);
                writefuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        //发送的数据包之和
                        //totalSendBytes.getAndAdd(sendBytes);
                        sendTotalPacketNum.getAndIncrement();
                        int serialNumber = packet.getSerialNumber();
                        commandCache.add_5(serialNumber + "_" + terminalId, terminalId);
                        commandCache.add_10(serialNumber + "_" + terminalId, terminalId);
                        commandCache.add_15(serialNumber + "_" + terminalId, terminalId);
                        commandCache.add_30(serialNumber + "_" + terminalId, terminalId);
                    }
                });

                //channel.write(ByteBuffer.wrap(message));
                //StatisticalData.sendPacketNumber++;
                // StatisticalData.sendTotalPacketNumber++;
                // StatisticalData.upFlow += message.length / 1024d;
                //StatisticalData.upTotalFlow += message.length / 1024d;
            }
        } catch (Exception e) {
            status.setChannelActive(false);
            logger.error("发送消息链路异常：" + e.getMessage());
            e.printStackTrace();
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    public void builder(Packet packet) {
        byte[] message;
        byte[] body = packet.getMessageBody();
        byte[] data = new byte[13 + body.length];
        ArraysUtils.arrayappend(data, 0, Convert.intTobytes(packet.getMessageId(), 2));
        ArraysUtils.arrayappend(data, 2, Convert.intTobytes(body.length, 2));
        ArraysUtils.arrayappend(data, 4, Convert.hexStringToBytes(Convert.preFillZero(packet.getTerminalId(), 12)));
        ArraysUtils.arrayappend(data, 10, Convert.intTobytes(packet.getSerialNumber(), 2));
        ArraysUtils.arrayappend(data, 12, body);
        byte checkCode = PacketProcessing.checkPackage(data, 0, data.length - 1);
        data[data.length - 1] = checkCode;
        byte[] bytes = PacketProcessing.escape(data, PacketProcessing.escapeByte, PacketProcessing.toEscapeByte);
        message = new byte[bytes.length + 2];
        message[0] = 0x7E;
        ArraysUtils.arrayappend(message, 1, bytes);
        message[message.length - 1] = 0x7E;
        MessageInfo info = new MessageInfo();
        info.setId(packet.getMessageId());
        info.setSerial(packet.getSerialNumber());
        info.setSendTime(new Date().getTime());
        cacheManager.setMessageCache(packet.getTerminalId(), info);
        TerminalStatus status = cacheManager.getTerminalCache().get(packet.getTerminalId());

        write(status, message, packet);
    }
}
