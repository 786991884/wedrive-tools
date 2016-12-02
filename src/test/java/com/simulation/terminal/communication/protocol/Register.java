package com.simulation.terminal.communication.protocol;

import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.util.Packet;

public class Register extends Command {

	@Override
	public void processor(Packet packet) {
		// TODO Auto-generated method stub
		MessageSend sendMessage = new MessageSend();
		sendMessage.builder(packet);
	}
}
