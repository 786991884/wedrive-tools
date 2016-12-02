package com.terminal.communication.protocol;


import com.terminal.cache.CacheManager;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.MessageSend;
import com.terminal.communication.TaCommand;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@LocationCommand(id = 0x8F40)
public class CanTamperControl extends TaCommand {

    @Autowired
    private MessageSend messageSend;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void processor(Packet packet) {
        byte[] body = packet.getMessageBody();
        byte[] answer = new byte[5];
        Random random = new Random();
        ArraysUtils.arrayappend(answer, 0, Convert.intTobytes(packet.getSerialNumber(), 2));
        ArraysUtils.arrayappend(answer, 2, new byte[]{(byte) random.nextInt(4)});
        ArraysUtils.arrayappend(answer, 3, new byte[]{1});
        ArraysUtils.arrayappend(answer, 4, new byte[]{body[1]});
        packet.setMessageBody(answer);
        packet.setMessageId(0x0F40);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        messageSend.builder(packet);
    }

}
