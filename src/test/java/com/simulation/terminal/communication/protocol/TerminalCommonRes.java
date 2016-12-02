package com.simulation.terminal.communication.protocol;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;

public class TerminalCommonRes extends Command {

	@Override
	public void processor(Packet packet) {
		byte[] message = new byte[5];
		ArraysUtils.arrayappend(message, 0, Convert.intTobytes(packet.getSerialNumber(), 2));
		ArraysUtils.arrayappend(message, 2, Convert.intTobytes(packet.getMessageId(), 2));
		message[4] = 0x00;
		packet.setMessageId(0x0001);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setMessageBody(message);
		MessageSend send = new MessageSend();
		send.builder(packet);
	}

}
