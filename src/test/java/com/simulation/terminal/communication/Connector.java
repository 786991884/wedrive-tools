package com.simulation.terminal.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.simulation.terminal.TerminalsStart;
import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.entity.TerminalStatus;

public class Connector {
	public static Logger logger = Logger.getLogger(TerminalsStart.class);
	public void connecting(long terminalId, String ip, int port){
		TerminalStatus status = CacheManager.getTerminalCache().get(terminalId);
		try {
			status.setChannelIsConnecting(true);
			SocketChannel channel = SocketChannel.open(new InetSocketAddress(ip, port));
			if(!channel.isOpen()){
				status.setChannelActive(false);
			}
			channel.configureBlocking(false);
			Selector selector = CacheManager.getSelector();
			channel.register(selector, SelectionKey.OP_READ);
			if(channel.isConnectionPending()){
				channel.finishConnect();
			}
			logger.info(terminalId + ": 建立TCP链路成功, 链路标识为： " + channel.hashCode());
			status.setChannel(channel);
			status.setChannelActive(true);
			status.setChannelIsConnecting(false);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("建立链路异常："+e.getMessage());
		}
	}
	
	public void reconnecting(long terminalId, String ip, int port){
		TerminalStatus status = CacheManager.getTerminalCache().get(terminalId);
		try {
			SocketChannel channel = SocketChannel.open(new InetSocketAddress(ip, port));
			if(!channel.isOpen()){
				status.setChannelActive(false);
			}
			channel.configureBlocking(false);
			Selector selector = CacheManager.getSelector();
			channel.register(selector, SelectionKey.OP_CONNECT);
			channel.register(selector, SelectionKey.OP_READ);
			if(channel.isConnectionPending()){
				channel.finishConnect();
			}
			StatisticalData.connectTimes++;
			StatisticalData.connectTotalTimes++;
			status.setChannel(channel);
			status.setChannelActive(true);
			status.setDisconnect(false);
			logger.info(terminalId + ": 重连TCP链路成功, 链路标识为： " + channel.hashCode());
		} catch (IOException e) {
			logger.error("重新建立链路异常："+e.getMessage());
			e.printStackTrace();
		}
	}
}
