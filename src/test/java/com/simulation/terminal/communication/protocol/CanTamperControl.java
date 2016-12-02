package com.simulation.terminal.communication.protocol;

import java.util.Random;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;

public class CanTamperControl extends Command {

	@Override
	public void processor(Packet packet) {
		byte[] body = packet.getMessageBody();
		byte[] answer = new byte[5];
		Random random = new Random();
		ArraysUtils.arrayappend(answer, 0, Convert.intTobytes(packet.getSerialNumber(), 2));
		ArraysUtils.arrayappend(answer, 2, new byte[]{(byte)random.nextInt(4)});
		ArraysUtils.arrayappend(answer, 3, new byte[]{1});
		ArraysUtils.arrayappend(answer, 4, new byte[]{body[1]});
		packet.setMessageBody(answer);
		packet.setMessageId(0x0F40);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		MessageSend send = new MessageSend();
		send.builder(packet);
	}

}
