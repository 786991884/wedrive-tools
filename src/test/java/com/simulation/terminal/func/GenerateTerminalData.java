package com.simulation.terminal.func;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.Connector;
import com.simulation.terminal.entity.LocationData;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.util.Packet;
import com.simulation.terminal.util.ServerConfig;

public class GenerateTerminalData implements Runnable {
	public static Logger logger = Logger.getLogger(GenerateTerminalData.class);
	@Override
	public void run() {
		try {
			while(true){
				for(int i=0; i<CacheManager.getTerminals().length; i++){
					long terminalId = CacheManager.getTerminals()[i];
					TerminalStatus status = CacheManager.getTerminalCache().get(terminalId);
					String auth = status.getTerminalAuthCode();
					auth="false";
					status.setAuth(true);
					if(auth == null || auth.equals("")){
						if(!status.isCancel() && ServerConfig.terminalCancel == 1){
							terminalCancel(terminalId);
						}else{
							terminalRegister(terminalId);
						}
					}else{
						if(!status.isAuth()){
							terminalAuth(terminalId, status);
						}else{
							generateLocationAndHeartbeateData(terminalId, status);
						}
					}
					if (i % ServerConfig.terminalBatchNumber == 0) {
						Thread.sleep(ServerConfig.terminalBatchInterval);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void terminalCancel(long terminalId){
		Packet packet = new Packet();
		packet.setMessageId(0x0003);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setTerminalId(terminalId);
		packet.setMessageBody(new byte[]{});
		Command command = CacheManager.getCommand(packet.getMessageId());
		if(command != null){
			command.processor(packet);
		}
	}
	
	public void terminalRegister(long terminalId){
		Packet packet = new Packet();
		packet.setMessageId(0x0100);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setTerminalId(terminalId);
		packet.setMessageBody(CacheManager.getRegisterData().get(terminalId).formatRegisterData());
		Command command = CacheManager.getCommand(packet.getMessageId());
		if(command != null){
			command.processor(packet);
		}
	}
	
	public void terminalAuth(long terminalId, TerminalStatus status){
		Packet packet = new Packet();
		packet.setMessageId(0x0102);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setTerminalId(terminalId);
		try {
			packet.setMessageBody(status.getTerminalAuthCode().getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Command command = CacheManager.getCommand(packet.getMessageId());
		if(command != null){
			command.processor(packet);
		}
	}
	
	public void generateLocationAndHeartbeateData(long terminalId, TerminalStatus terminalStatus){
		long current = new Date().getTime()/1000;
		long status = 0;
		if(current - terminalStatus.getBeginData() > ServerConfig.terminalContinue){
			if(getProbability(ServerConfig.statusAccSwitchPercentage)){
				status = ServerConfig.gpsDataStatusDefault | 0x01;
			}else{
				status = ServerConfig.gpsDataStatusDefault;
			}
			terminalStatus.setAccStatus(status);
		}else{
			status = terminalStatus.getAccStatus();
		}
		if(terminalStatus.isAlarm()){
			if(current - terminalStatus.getBeginData() < ServerConfig.terminalContinue){
//				heartbeatData(terminalId);
				getLocationData(terminalId, true, terminalStatus.getAlarm(), status);
			}else{
				terminalStatus.setAlarm(false);
//				heartbeatData(terminalId);
				getLocationData(terminalId, true, 0, status);
			}
		}else if(terminalStatus.isError()){
			if(current - terminalStatus.getBeginData() < ServerConfig.terminalContinue){
//				heartbeatData(terminalId);
				getLocationData(terminalId, false, 0, status);
				StatisticalData.errorPacketNumber++;
				StatisticalData.errorTotalPacketNumber++;
			}else{
				terminalStatus.setError(false);
//				heartbeatData(terminalId);
				getLocationData(terminalId, true, 0, status);
			}
		}else{
			Random random = new Random();
			int selectIndex = random.nextInt(3);
			if (selectIndex == 0) {
				if (getProbability(ServerConfig.terminalAlarmPercentage)) {
					long alarms = GenerateAlarm.getRandomAlarm(ServerConfig.gpsDataAlarmNumber, ServerConfig.gpsDataAlarmsDefault);
					terminalStatus.setAlarm(true);
					terminalStatus.setAlarm(alarms);
					terminalStatus.setBeginData(current);
//					heartbeatData(terminalId);
					getLocationData(terminalId, true, alarms, status);
				} else {
//					heartbeatData(terminalId);
					getLocationData(terminalId, true, 0, status);
				}
			} else if (selectIndex == 1) {
				if (getProbability(ServerConfig.terminalErrorDataPercentage)) {
					terminalStatus.setError(true);
					terminalStatus.setBeginData(current);
//					heartbeatData(terminalId);
					getLocationData(terminalId, false, 0, status);
					StatisticalData.errorPacketNumber++;
					StatisticalData.errorTotalPacketNumber++;
				} else {
//					heartbeatData(terminalId);
					getLocationData(terminalId, true, 0, status);
				}
			} else {
				if (getProbability(ServerConfig.terminalLinkDisconnectPercentage)) {
					terminalStatus.setDisconnect(true);
					terminalStatus.setBeginData(0);
					try {
						SocketChannel channel = terminalStatus.getChannel();
						channel.close();
						channel = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
					StatisticalData.disconnectTimes++;
					StatisticalData.disconnectTotalTimes++;
					Connector connector = new Connector();
					connector.reconnecting(terminalId, ServerConfig.serverTcpIP, ServerConfig.serverTcpPort);
				} else {
//					heartbeatData(terminalId);
					getLocationData(terminalId, true, 0, status);
				}
			}
		}
	}
	
	public void heartbeatData(long terminalId){
		Packet packet = new Packet();
		packet.setMessageId(0x0002);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setTerminalId(terminalId);
		packet.setMessageBody(new byte[]{});
		Command command = CacheManager.getCommand(packet.getMessageId());
		command.processor(packet);
	}
	/**
	 * type: true 正常位置数据；false 异常位置数据
	 * @param index
	 * @param type
	 */
	public void getLocationData(long terminalId, boolean type, long alarm, long status){
		Packet packet = new Packet();
		packet.setMessageId(0x0200);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setTerminalId(terminalId);
		if(type){
			packet.setMessageBody(builderLocationData(terminalId, alarm, status).formatLocationData());
		}else{
			packet.setMessageBody(builderLocationData(terminalId, alarm, status).formatErrorLocationData());
		}
		Command command = CacheManager.getCommand(packet.getMessageId());
		command.processor(packet);
	}
	
	public LocationData builderLocationData(long terminalId, long alarm, long status){
		LocationData data = new LocationData();
		data.setAlarm(alarm);
		data.setStatus(status);
		Random random = new Random();
		data.setAltitude(random.nextInt(ServerConfig.gpsDataAltitudeMax));
		data.setDirection(random.nextInt(360));
		data.setGpsDate(new Date().getTime());
		data.setSpeed(random.nextInt(ServerConfig.gpsDataSpeedMax));
		data.setLatitude(CacheManager.getLatitude());
		data.setLongitude(CacheManager.getLongitude());
		data.setMileage(CacheManager.getMileage());
		return data;
	}
	
	public static boolean getProbability(int percentage){
		Random random = new Random();
		if(random.nextInt(100) < percentage){
			return true;
		}else{
			return false;
		}
	}
}
