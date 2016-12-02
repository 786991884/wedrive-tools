package com.simulation.terminal.cache;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.simulation.terminal.communication.Command;
import com.simulation.terminal.entity.MessageInfo;
import com.simulation.terminal.entity.RegisterData;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.ServerConfig;

public class CacheManager {
	private static final int latlngChangeLimit = 8000000;
	private static int serialNumber = 0;
	private static int interval = 10;
	private final static int randomLat = 5166341;
	private final static int randomLng = 6042480;
	private final static int randomPrecision = 1000;
	
	public static int getSerialNumber(){
		if(serialNumber < 0xFFFF){
			serialNumber++;
		}else{
			serialNumber = 0;
		}
		return serialNumber;
	}
	private static long[] terminals = new long[ServerConfig.terminalNumber];
	private static Map<Long, TerminalStatus> terminalCache = new ConcurrentHashMap<Long, TerminalStatus>();
	private static Map<Integer, byte[]> terminalMessageCache = new ConcurrentHashMap<Integer, byte[]>();
	private static Map<Long, RegisterData> terminalRegisterDataCache = new ConcurrentHashMap<Long, RegisterData>();
	private static Map<Integer, Command> commandCache = new HashMap<Integer, Command>();
	private static Map<Long, String> authCode = new ConcurrentHashMap<Long, String>();
	public static long[] getTerminals(){
		return terminals;
	}
	
	public static Map<Long, TerminalStatus> getTerminalCache() {
		return terminalCache;
	}

	public static void setTerminalCache(Map<Long, TerminalStatus> cache ) {
		terminalCache = cache;
	}
	
	public static Map<Integer, byte[]> getMessageCache(){
		return terminalMessageCache;
	}
	
	public static void setMessageCache(Integer channelId, byte[] data){
		terminalMessageCache.put(channelId, data);
	}
	
	public static Map<Long, RegisterData> getRegisterData(){
		return terminalRegisterDataCache;
	}
	
	public static void setRegisterDataCache(Long terminalId, RegisterData data){
		terminalRegisterDataCache.put(terminalId, data);
	}
	
	public static void setCommand(Integer commandId, Command command){
		commandCache.put(commandId, command);
	}
	
	public static Command getCommand(Integer commandId){
		return commandCache.get(commandId);
	}
	
	private static int latitude = ServerConfig.gpsDataLatitude;
	private static int longitude = ServerConfig.gpsDataLongitude;
	public static int getLatitude(){
		latitude += ServerConfig.gpsDataLatitudeIncrease;
		if(latitude - ServerConfig.gpsDataLatitude > latlngChangeLimit){
			latitude = ServerConfig.gpsDataLatitude;
		}
//		latitude = ServerConfig.gpsDataLatitude;
//		latitude += new Random().nextInt(randomLat/randomPrecision)*randomPrecision;
		return latitude;
	}
	
	public static int getLongitude(){
		longitude += ServerConfig.gpsDataLongitudeIncrease;
		if(longitude - ServerConfig.gpsDataLongitude > latlngChangeLimit){
			longitude = ServerConfig.gpsDataLongitude;
		}
//		longitude = ServerConfig.gpsDataLongitude;
//		longitude += new Random().nextInt(randomLng/randomPrecision)*randomPrecision;
		return longitude;
	}
	
	private static Selector selector = null;

	public static Selector getSelector() {
		if(selector == null){
			try {
				selector = Selector.open();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return selector;
	}
	
	private static Map<Long, LinkedList<MessageInfo>> messageCache = new ConcurrentHashMap<Long,LinkedList<MessageInfo>>();
	
	public static void setMessageCache(Long terminalId, MessageInfo info){
		LinkedList<MessageInfo> list = messageCache.get(terminalId);
		if(list != null){
			if(list.size() >= interval){
				list.removeLast();
				list.addFirst(info);
			}else{
				list.addFirst(info);
			}
		}else{
			list = new LinkedList<>();
			list.addFirst(info);
			messageCache.put(terminalId, list);
		}
	}
	
	public static long getSendTime(Long terminalId, int serial, int commandId){
		LinkedList<MessageInfo> list = messageCache.get(terminalId);
		if(list != null){
			Iterator<MessageInfo> iter = list.iterator();
			while(iter.hasNext()){
				MessageInfo info = iter.next();
				if(info.getId() == commandId && info.getSerial() == serial){
					return info.getSendTime();
				}
			}
		}
		return 0;
	}

	public static Map<Long, String> getAuthCode() {
		return authCode;
	}

	public static void setAuthCode(Map<Long, String> authCode) {
		CacheManager.authCode = authCode;
	}
	
	private static long mileage = 0;
	private static long mileageInterval = 1;
	private static int canMileage = 1000;
	
	public static long getMileage(){
		if(mileageInterval % ServerConfig.terminalNumber == 0){
			mileage += 10;
		}
		mileageInterval++;
		return mileage;
	}
	
	public static byte[] getCanMileage(){
		if(canMileage >= Integer.MAX_VALUE){
			canMileage = 1000;
		}else{
			canMileage ++;
		}
		return Convert.longTobytes(canMileage, 4);
	}
}
