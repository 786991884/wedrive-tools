package com.terminal.netty;

import com.terminal.cache.CacheManager;
import com.terminal.cache.CommandCache;
import com.terminal.communication.ProtocolDispatcher;
import com.terminal.communication.TaCommand;
import com.terminal.communication.protocol.TerminalCommonRes;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import com.terminal.util.PacketProcessing;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Lenovo
 * @date 2016-11-17
 * @modify
 * @copyright
 */
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    public static AtomicLong reveiveTotalPacketNum = new AtomicLong(0);

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    private CommandCache commandCache;

    @Autowired
    private CacheManager cacheManager;

    private static final byte SIGN_7E = 0X7E;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        byte[] packet = (byte[]) o;
        if (packet != null) {
            if (packet[0] == SIGN_7E && packet[packet.length - 1] == SIGN_7E && packet.length > 13) {
                reveiveTotalPacketNum.getAndIncrement();
                dealPacket(packet);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    private void dealPacket(byte[] srcBytes) {
        //StatisticalData.receivePacketNumber++;
        //StatisticalData.receiveTotalPacketNumber++;
        //StatisticalData.downFlow = srcBytes.length / 1024d;
        //StatisticalData.downTotalFlow = srcBytes.length / 1024d;

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
        long terminalId = Long.parseLong(Convert.bytesToHexString(tid));
        packet.setTerminalId(terminalId);
        byte[] serial = ArraysUtils.subarrays(bytes, 10, 2);
        int serialNumber = Convert.byte2Int(serial, 2);
        packet.setSerialNumber(serialNumber);
        packet.setMessageBody(ArraysUtils.subarrays(bytes, 12, bytes.length - 13));
        logger.info(terminalId + " 收到消息 : " + Convert.bytesToHexString(srcBytes));
        if (commandCache.get_5(serialNumber + "_" + terminalId) != null) {
            commandCache.remove_5(serialNumber + "_" + terminalId);
        }
        if (commandCache.get_10(serialNumber + "_" + terminalId) != null) {
            commandCache.remove_10(serialNumber + "_" + terminalId);
        }
        if (commandCache.get_15(serialNumber + "_" + terminalId) != null) {
            commandCache.remove_15(serialNumber + "_" + terminalId);
        }
        if (commandCache.get_30(serialNumber + "_" + terminalId) != null) {
            commandCache.remove_30(serialNumber + "_" + terminalId);
        }
        TaCommand command = this.protocolDispatcher.getHandler(packet.getMessageId());
        logger.info("messageId:" + packet.getMessageId());
        if (command != null) {
            command.processor(packet);
        } else {
            //TODO:为了测试：对于平台下发指令的终端通用应答，回复成功。
            command = new TerminalCommonRes();
            command.processor(packet);
            logger.info("protocol cann't find..." + packet.getMessageId());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                /*读超时*/
                logger.info("READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*写超时*/
                logger.info("WRITER_IDLE 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                logger.info("ALL_IDLE 总超时");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive:Reconnect");
        final EventLoopGroup loop = ctx.channel().eventLoop();
    }
}
