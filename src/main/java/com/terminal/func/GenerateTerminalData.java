package com.terminal.func;

import com.terminal.cache.CacheManager;
import com.terminal.cache.StatisticalData;
import com.terminal.communication.ProtocolDispatcher;
import com.terminal.communication.TaCommand;
import com.terminal.entity.LocationData;
import com.terminal.entity.TerminalStatus;
import com.terminal.util.ConfigUtils;
import com.terminal.util.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GenerateTerminalData {
    public static Logger logger = LoggerFactory.getLogger(GenerateTerminalData.class);

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Value("${generate.terminal.data.thread.number:30}")
    private int threadNum;

    private ExecutorService executorService;

    public static LocalDateTime start = LocalDateTime.now();
    public static LocalDateTime end = LocalDateTime.now();

    @PostConstruct
    public void initExecutor() {
        executorService = Executors.newFixedThreadPool(threadNum);
    }

    public void init() {

        int index = 0;
        int total = cacheManager.getTerminals().length;
        // 计算N个线程，每个线程大概的数据量
        int tn = total > threadNum ? (total / threadNum) : total;
        CyclicBarrier barrier = new CyclicBarrier(threadNum);
        Map<Integer, Task> mapTask = new HashMap<>();
        Task task;
        for (int i = 0; i < threadNum; i++, index += tn) {
            int start = index;
            if (start >= total) {
                break;
            }
            int end = start + tn;
            end = (end > total || i == threadNum - 1) ? total : end;
            task = new Task(barrier, start, end);
            mapTask.put(i, task);
           /* Future<Long> f = (Future<Long>) executorService.submit(new FutureTask<>(task));
            try {
                Long aLong = f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            executorService.execute(task);
        }
        try {
            if (LocalDateTime.now().getSecond() == 0) {
                Thread.sleep(30 * 1000);
            }
            boolean c = true;
            while (c) {
                if (LocalDateTime.now().getSecond() == 0) {
                    c = false;
                } else {
                    Thread.sleep(2);
                }
            }
            start = LocalDateTime.now();
            end = start.plusSeconds(configUtils.getPressureTime() * 60);
            Thread.sleep(configUtils.getPressureTime() * 60 * 1000);
            //Thread.sleep(configUtils.getTerminalBatchInterval());
            for (Task value : mapTask.values()) {
                value.setRunning(false);
                logger.info("isRunning的值已经被设置了false");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Task implements Runnable {
        private CyclicBarrier barrier;
        private int start;
        private int end;
        private volatile boolean isRunning = true;

        private void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        public Task(CyclicBarrier barrier, int start, int end) {
            this.barrier = barrier;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try {
                logger.info("准备OK.");
                int interval = configUtils.getTerminalBatchInterval();
                int times = (configUtils.getPressureTime() * 60 * 1000) / interval;
                LocalDateTime now;
                LocalDateTime afterInterval;
                boolean b;
                int s = 1;
                barrier.await();
                if (LocalDateTime.now().getSecond() == 0) {
                    Thread.sleep(30 * 1000);
                }
                boolean c = true;
                while (c) {
                    if (LocalDateTime.now().getSecond() == 0) {
                        c = false;
                    } else {
                        Thread.sleep(2);
                    }
                }
                now = LocalDateTime.now();
                //isRunning == true ||
                while (isRunning == true && s <= times) {
                    // 1 纳秒 = 1000皮秒 1,000 纳秒 = 1微秒 1,000,000 纳秒 = 1毫秒 1,000,000,000 纳秒 = 1秒
                    afterInterval = now.plusNanos((long) (s * interval * Math.pow(10, 6)));
                    //当时自然秒
                    for (int i = start; i < end; i++) {
                        long terminalId = cacheManager.getTerminals()[i];
                        TerminalStatus status = cacheManager.getTerminalCache().get(terminalId);
                        String auth = status.getTerminalAuthCode();
                        if (configUtils.isTerminalAuth()) {
                            if (auth == null || auth.equals("")) {
                                if (!status.isCancel() && configUtils.getTerminalCancel() == 1) {
                                    terminalCancel(terminalId);
                                } else {
                                    terminalRegister(terminalId);
                                }
                            } else {
                                if (!status.isAuth()) {
                                    terminalAuth(terminalId, status);
                                } else {
                                    generateLocationAndHeartbeateData(terminalId, status);
                                }
                            }
                        } else if (configUtils.isTerminalLocationData()) {
                            generateLocationAndHeartbeateData(terminalId, status);
                        } else {
                            terminalRegister(terminalId);
                            /*if (auth == null || auth.equals("")) {
                                if (!status.isCancel() && configUtils.getTerminalCancel() == 1) {
                                    terminalCancel(terminalId);
                                } else {
                                    terminalRegister(terminalId);
                                }
                            } else {
                                if (!status.isAuth()) {
                                    terminalAuth(terminalId, status);
                                }
                            }*/
                        }
                        //if (i % configUtils.getTerminalBatchNumber() == 0) {
                        //if (i % (end - 1) == 0) {
                        //Thread.sleep(configUtils.getTerminalBatchInterval());
                        //}
                    }
                    s++;
                    b = LocalDateTime.now().isBefore(afterInterval);
                    while (b) {
                        Thread.sleep(2);
                        b = LocalDateTime.now().isBefore(afterInterval);
                    }
                }
                logger.info("线程停止");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void terminalCancel(long terminalId) {
        Packet packet = new Packet();
        packet.setMessageId(0x0003);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setTerminalId(terminalId);
        packet.setMessageBody(new byte[]{});
        TaCommand handler = protocolDispatcher.getHandler(packet.getMessageId());
        if (handler != null) {
            handler.processor(packet);
        }
    }

    public void terminalRegister(long terminalId) {
        Packet packet = new Packet();
        packet.setMessageId(0x0100);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setTerminalId(terminalId);
        packet.setMessageBody(cacheManager.getRegisterData().get(terminalId).formatRegisterData());
        TaCommand handler = protocolDispatcher.getHandler(packet.getMessageId());
        if (handler != null) {
            handler.processor(packet);
        }
    }

    public void terminalAuth(long terminalId, TerminalStatus status) {
        Packet packet = new Packet();
        packet.setMessageId(0x0102);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setTerminalId(terminalId);
        try {
            packet.setMessageBody(status.getTerminalAuthCode().getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        TaCommand handler = protocolDispatcher.getHandler(packet.getMessageId());
        if (handler != null) {
            handler.processor(packet);
        }
    }

    public void generateLocationAndHeartbeateData(long terminalId, TerminalStatus terminalStatus) {
        long current = new Date().getTime() / 1000;
        long status = 0;
        if (current - terminalStatus.getBeginData() > configUtils.getTerminalContinue()) {
            if (getProbability(configUtils.getStatusAccSwitchPercentage())) {
                status = configUtils.getGpsDataStatusDefault() | 0x01;
            } else {
                status = configUtils.getGpsDataStatusDefault();
            }
            terminalStatus.setAccStatus(status);
        } else {
            status = terminalStatus.getAccStatus();
        }
        if (terminalStatus.isAlarm()) {
            if (current - terminalStatus.getBeginData() < configUtils.getTerminalContinue()) {
//				heartbeatData(terminalId);
                getLocationData(terminalId, true, terminalStatus.getAlarm(), status);
            } else {
                terminalStatus.setAlarm(false);
//				heartbeatData(terminalId);
                getLocationData(terminalId, true, 0, status);
            }
        } else if (terminalStatus.isError()) {
            if (current - terminalStatus.getBeginData() < configUtils.getTerminalContinue()) {
//				heartbeatData(terminalId);
                getLocationData(terminalId, false, 0, status);
                StatisticalData.errorPacketNumber++;
                StatisticalData.errorTotalPacketNumber++;
            } else {
                terminalStatus.setError(false);
//				heartbeatData(terminalId);
                getLocationData(terminalId, true, 0, status);
            }
        } else {
            Random random = new Random();
            int selectIndex = random.nextInt(3);
            if (selectIndex == 0) {
                if (getProbability(configUtils.getTerminalAlarmPercentage())) {
                    long alarms = GenerateAlarm.getRandomAlarm(configUtils.getGpsDataAlarmNumber(), configUtils.getGpsDataAlarmsDefault());
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
                if (getProbability(configUtils.getTerminalErrorDataPercentage())) {
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
//					heartbeatData(terminalId);
                getLocationData(terminalId, true, 0, status);
            }
        }
    }

    public void heartbeatData(long terminalId) {
        Packet packet = new Packet();
        packet.setMessageId(0x0002);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setTerminalId(terminalId);
        packet.setMessageBody(new byte[]{});
        TaCommand handler = protocolDispatcher.getHandler(packet.getMessageId());
        if (handler != null) {
            handler.processor(packet);
        }
    }

    /**
     * type: true 正常位置数据；false 异常位置数据
     *
     * @param
     * @param type
     */
    public void getLocationData(long terminalId, boolean type, long alarm, long status) {
        Packet packet = new Packet();
        packet.setMessageId(0x0200);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        packet.setTerminalId(terminalId);
        if (type) {
            packet.setMessageBody(builderLocationData(terminalId, alarm, status).formatLocationData());
        } else {
            packet.setMessageBody(builderLocationData(terminalId, alarm, status).formatErrorLocationData());
        }
        TaCommand handler = protocolDispatcher.getHandler(packet.getMessageId());
        if (handler != null) {
            handler.processor(packet);
        }
    }

    public LocationData builderLocationData(long terminalId, long alarm, long status) {
        LocationData data = new LocationData();
        data.setAlarm(alarm);
        data.setStatus(status);
        Random random = new Random();
        data.setAltitude(random.nextInt(configUtils.getGpsDataAltitudeMax()));
        data.setDirection(random.nextInt(360));
        data.setGpsDate(new Date().getTime());
        data.setSpeed(random.nextInt(configUtils.getGpsDataSpeedMax()));
        data.setLatitude(cacheManager.getLatitude());
        data.setLongitude(cacheManager.getLongitude());
        data.setMileage(cacheManager.getMileage());
        return data;
    }

    public static boolean getProbability(int percentage) {
        Random random = new Random();
        if (random.nextInt(100) < percentage) {
            return true;
        } else {
            return false;
        }
    }
}
