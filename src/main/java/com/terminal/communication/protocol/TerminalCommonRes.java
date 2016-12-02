package com.terminal.communication.protocol;


import com.terminal.cache.CacheManager;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.MessageSend;
import com.terminal.communication.TaCommand;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import org.springframework.beans.factory.annotation.Autowired;

@LocationCommand(id = 0x0001)
public class TerminalCommonRes extends TaCommand {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MessageSend messageSend;

    @Override
    public void processor(Packet packet) {
        byte[] message = new byte[5];
        ArraysUtils.arrayappend(message, 0, Convert.intTobytes(packet.getSerialNumber(), 2));
        ArraysUtils.arrayappend(message, 2, Convert.intTobytes(packet.getMessageId(), 2));
        message[4] = 0x00;
        packet.setMessageId(0x0001);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setMessageBody(message);
        messageSend.builder(packet);
    }

}
