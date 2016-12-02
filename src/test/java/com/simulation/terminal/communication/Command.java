package com.simulation.terminal.communication;

import com.simulation.terminal.util.Packet;


public abstract class Command {
	/**
	 * 接收消息处理类
	 * @param packet
	 */
	public abstract void processor(Packet packet);
}
