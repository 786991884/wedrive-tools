package com.terminal.communication.protocol;

import com.terminal.communication.LocationCommand;
import com.terminal.communication.MessageSend;
import com.terminal.communication.TaCommand;
import com.terminal.util.Packet;
import org.springframework.beans.factory.annotation.Autowired;

@LocationCommand(id = 0x0002)
public class HeartBeat extends TaCommand {

    @Autowired
    private MessageSend messageSend;

    @Override
    public void processor(Packet packet) {
        messageSend.builder(packet);
    }
}
