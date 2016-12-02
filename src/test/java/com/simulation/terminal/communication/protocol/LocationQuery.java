package com.simulation.terminal.communication.protocol;

import java.util.Date;
import java.util.Random;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.entity.LocationData;
import com.simulation.terminal.util.Packet;
import com.simulation.terminal.util.ServerConfig;

public class LocationQuery extends Command {

	@Override
	public void processor(Packet packet) {
		packet.setMessageId(0x0201);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		packet.setMessageBody(builderLocationData(packet.getTerminalId(), ServerConfig.gpsDataAlarmsDefault, ServerConfig.gpsDataStatusDefault).formatLocationData());
		MessageSend send = new MessageSend();
		send.builder(packet);
	}

	public LocationData builderLocationData(long terminalId, long alarm, long status){
		LocationData data = new LocationData();
		data.setAlarm(alarm);
		data.setStatus(status);
		Random random = new Random();
		data.setAltitude(random.nextInt(ServerConfig.gpsDataAltitudeMax));
		data.setDirection(random.nextInt(360));
		data.setGpsDate(new Date().getTime());
		data.setSpeed(1000+random.nextInt(ServerConfig.gpsDataSpeedMax));
		data.setLatitude(CacheManager.getLatitude());
		data.setLongitude(CacheManager.getLongitude());
		data.setMileage(CacheManager.getMileage());
		return data;
	}
}
