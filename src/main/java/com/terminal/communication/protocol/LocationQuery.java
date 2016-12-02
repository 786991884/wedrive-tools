package com.terminal.communication.protocol;


import com.terminal.cache.CacheManager;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.MessageSend;
import com.terminal.communication.TaCommand;
import com.terminal.entity.LocationData;
import com.terminal.util.ConfigUtils;
import com.terminal.util.Packet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Random;

@LocationCommand(id = 0x8201)
public class LocationQuery extends TaCommand {

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MessageSend messageSend;

    @Override
    public void processor(Packet packet) {
        packet.setMessageId(0x0201);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setMessageBody(builderLocationData(packet.getTerminalId(), configUtils.getGpsDataAlarmsDefault(), configUtils.getGpsDataStatusDefault()).formatLocationData());
        messageSend.builder(packet);
    }

    public LocationData builderLocationData(long terminalId, long alarm, long status) {
        LocationData data = new LocationData();
        data.setAlarm(alarm);
        data.setStatus(status);
        Random random = new Random();
        data.setAltitude(random.nextInt(configUtils.getGpsDataAltitudeMax()));
        data.setDirection(random.nextInt(360));
        data.setGpsDate(new Date().getTime());
        data.setSpeed(1000 + random.nextInt(configUtils.getGpsDataSpeedMax()));
        data.setLatitude(cacheManager.getLatitude());
        data.setLongitude(cacheManager.getLongitude());
        data.setMileage(cacheManager.getMileage());
        return data;
    }
}
