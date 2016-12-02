package com.simulation.terminal.func;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.Connector;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.util.ServerConfig;

public class LinkReconnect implements Runnable {
	public static Logger logger = Logger.getLogger(LinkReconnect.class);
	@Override
	public void run() {
		while(true){
			try{
				for(int i=0; i<CacheManager.getTerminals().length; i++){
					long terminalId = CacheManager.getTerminals()[i];
					TerminalStatus status = CacheManager.getTerminalCache().get(terminalId);
					if(!status.isChannelActive() && !status.isChannelIsConnecting()){
						SocketChannel channel = status.getChannel();
						if(channel != null){
							status.setChannel(null);
							channel.close();
						}
						try{
							Connector connector = new Connector();
							connector.connecting(terminalId, ServerConfig.serverTcpIP, ServerConfig.serverTcpPort);
						}catch(Throwable cause){
							logger.error("Unexpected Throwable.", cause);
						}
					}
				}
				Thread.sleep(5000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
