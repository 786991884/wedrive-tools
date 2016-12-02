package com.terminal.cache;


import com.terminal.communication.TaCommand;
import com.terminal.entity.MessageInfo;
import com.terminal.entity.RegisterData;
import com.terminal.entity.TerminalStatus;
import com.terminal.util.ConfigUtils;
import com.terminal.util.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class CacheManager {
    private static final int latlngChangeLimit = 8000000;
    //private static int serialNumber = 0;
    private static AtomicInteger serialNumber = new AtomicInteger();
    private static int interval = 10;
    private final static int randomLat = 5166341;
    private final static int randomLng = 6042480;
    private final static int randomPrecision = 1000;


    /**
     * 生成器锁
     */
    private final ReentrantLock lock = new ReentrantLock();
    /**
     * 预生成锁
     */
    //private final ReentrantLock prepareLock = new ReentrantLock();

    @Autowired
    private ConfigUtils configUtils;

    public int getSerialNumber() {
        lock.lock();
        try {
        /*if (serialNumber.get() < 0xFFFF) {
            return serialNumber.getAndIncrement();
        }
        return serialNumber.getAndSet(0);*/
            return (serialNumber.get() < 0xFFFF) ? serialNumber.getAndIncrement() : serialNumber.getAndSet(0);
        } finally {
            lock.unlock();
        }
    }

    private static long[] terminals = null;
    private static Map<Long, TerminalStatus> terminalCache = new ConcurrentHashMap<>();
    private static Map<Integer, byte[]> terminalMessageCache = new ConcurrentHashMap<>();
    private static Map<Long, RegisterData> terminalRegisterDataCache = new ConcurrentHashMap<>();
    private static Map<Integer, TaCommand> commandCache = new HashMap<>();
    private static Map<Long, String> authCode = new ConcurrentHashMap<>();
    private static int latitude;
    private static int longitude;
    private static Map<Long, LinkedList<MessageInfo>> messageCache = new ConcurrentHashMap<Long, LinkedList<MessageInfo>>();

    @PostConstruct
    public void init() {
        terminals = new long[configUtils.getTerminalNumber()];
        latitude = configUtils.getGpsDataLatitude();
        longitude = configUtils.getGpsDataLongitude();
    }


    public long[] getTerminals() {
        return terminals;
    }

    public Map<Long, TerminalStatus> getTerminalCache() {
        return terminalCache;
    }

    public void setTerminalCache(Map<Long, TerminalStatus> cache) {
        terminalCache = cache;
    }

    public Map<Integer, byte[]> getMessageCache() {
        return terminalMessageCache;
    }

    public void setMessageCache(Integer channelId, byte[] data) {
        terminalMessageCache.put(channelId, data);
    }

    public Map<Long, RegisterData> getRegisterData() {
        return terminalRegisterDataCache;
    }

    public void setRegisterDataCache(Long terminalId, RegisterData data) {
        terminalRegisterDataCache.put(terminalId, data);
    }

    public void setCommand(Integer commandId, TaCommand command) {
        commandCache.put(commandId, command);
    }

    public TaCommand getCommand(Integer commandId) {
        return commandCache.get(commandId);
    }

    public int getLatitude() {
        latitude += configUtils.getGpsDataLatitudeIncrease();
        if (latitude - configUtils.getGpsDataLatitude() > latlngChangeLimit) {
            latitude = configUtils.getGpsDataLatitude();
        }
        return latitude;
    }

    public int getLongitude() {
        longitude += configUtils.getGpsDataLongitudeIncrease();
        if (longitude - configUtils.getGpsDataLongitude() > latlngChangeLimit) {
            longitude = configUtils.getGpsDataLongitude();
        }
        return longitude;
    }


    public void setMessageCache(Long terminalId, MessageInfo info) {
        LinkedList<MessageInfo> list = messageCache.get(terminalId);
        if (list != null) {
            if (list.size() >= interval) {
                list.removeLast();
                list.addFirst(info);
            } else {
                list.addFirst(info);
            }
        } else {
            list = new LinkedList<>();
            list.addFirst(info);
            messageCache.put(terminalId, list);
        }
    }

    public long getSendTime(Long terminalId, int serial, int commandId) {
        LinkedList<MessageInfo> list = messageCache.get(terminalId);
        if (list != null) {
            Iterator<MessageInfo> iter = list.iterator();
            while (iter.hasNext()) {
                MessageInfo info = iter.next();
                if (info.getId() == commandId && info.getSerial() == serial) {
                    return info.getSendTime();
                }
            }
        }
        return 0;
    }

    public Map<Long, String> getAuthCode() {
        return authCode;
    }

    public void setAuthCode(Map<Long, String> authCode) {
        CacheManager.authCode = authCode;
    }

    private static long mileage = 0;
    private static long mileageInterval = 1;
    private static int canMileage = 1000;

    public long getMileage() {
        if (mileageInterval % configUtils.getTerminalNumber() == 0) {
            mileage += 10;
        }
        mileageInterval++;
        return mileage;
    }

    public byte[] getCanMileage() {
        if (canMileage >= Integer.MAX_VALUE) {
            canMileage = 1000;
        } else {
            canMileage++;
        }
        return Convert.longTobytes(canMileage, 4);
    }
}
