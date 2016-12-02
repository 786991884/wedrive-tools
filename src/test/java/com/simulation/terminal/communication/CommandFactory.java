package com.simulation.terminal.communication;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.protocol.TerminalCommonRes;
import com.simulation.terminal.util.Packet;


public class CommandFactory {
	
	public static void processer(Packet packet){
        System.out.println("messageId:"+packet.getMessageId());
		Command command = CacheManager.getCommand(packet.getMessageId());
		if(command != null){
			command.processor(packet);
		}else{
			//TODO:为了测试：对于平台下发指令的终端通用应答，回复成功。
			command = new TerminalCommonRes();
			command.processor(packet);

			System.out.println("protocol cann't find..."+packet.getMessageId());
		}
	}
}
