package com.simulation.terminal.communication.protocol;

import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.util.Packet;

public class Cancel extends Command {

	@Override
	public void processor(Packet packet) {
		MessageSend sendMessage = new MessageSend();
		sendMessage.builder(packet);
	}

}
