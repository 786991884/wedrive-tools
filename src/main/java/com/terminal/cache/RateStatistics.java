package com.terminal.cache;

import com.terminal.communication.MessageSend;
import com.terminal.netty.NettyClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RateStatistics {
    private final static Logger log = LoggerFactory.getLogger(RateStatistics.class);
    //private static final String second = "";

    public static long send1s = 0L;
    public static long send2s = 0L;
    public static long send3s = 0L;
    public static long send5s = 0L;
    public static long send10s = 0L;
    public static long read1s = 0L;
    public static long read2s = 0L;
    public static long read3s = 0L;
    public static long read5s = 0L;
    public static long read10s = 0L;

    public static String sendResult1s = "";
    public static String sendResult2s = "";
    public static String sendResult3s = "";
    public static String sendResult5s = "";
    public static String sendResult10s = "";
    public static String readResult1s = "";
    public static String readResult2s = "";
    public static String readResult3s = "";
    public static String readResult5s = "";
    public static String readResult10s = "";

    //发包速率
    public static final Map<Integer, String> chartMap_1 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMap_2 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMap_3 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMap_5 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMap_10 = new LinkedHashMap<>();//统计集合
    public static Integer chartCount_1 = 0;
    public static Integer chartCount_2 = 0;
    public static Integer chartCount_3 = 0;
    public static Integer chartCount_5 = 0;
    public static Integer chartCount_10 = 0;

    //读包速率
    public static final Map<Integer, String> chartMapS_1 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapS_2 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapS_3 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapS_5 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapS_10 = new LinkedHashMap<>();//统计集合
    public static Integer chartCountS_1 = 0;
    public static Integer chartCountS_2 = 0;
    public static Integer chartCountS_3 = 0;
    public static Integer chartCountS_5 = 0;
    public static Integer chartCountS_10 = 0;


    //发读包速率
    public static final Map<Integer, String> chartMapSR_1 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapSR_2 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapSR_3 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapSR_5 = new LinkedHashMap<>();//统计集合
    public static final Map<Integer, String> chartMapSR_10 = new LinkedHashMap<>();//统计集合

    @Scheduled(cron = "0/1 * * * * ? ")
    public void sendRate1s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            if (mysend != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = mysend - send1s;
                sendResult1s = numberFormat.format((float) rate / (float) 1);
                send1s = mysend;
                chartCount_1++;
                chartMap_1.put(chartCount_1, sendResult1s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/2 * * * * ? ")
    public void sendRate2s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            if (mysend != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = mysend - send2s;
                sendResult2s = numberFormat.format((float) rate / (float) 2);
                send2s = mysend;
                chartCount_2 += 2;
                chartMap_2.put(chartCount_2, sendResult2s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/3 * * * * ? ")
    public void sendRate3s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            if (mysend != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = mysend - send3s;
                sendResult3s = numberFormat.format((float) rate / (float) 3);
                send3s = mysend;
                chartCount_3 += 3;
                chartMap_3.put(chartCount_3, sendResult3s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ? ")
    public void sendRate5s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            if (mysend != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = mysend - send5s;
                sendResult5s = numberFormat.format((float) rate / (float) 5);
                send5s = mysend;
                chartCount_5 += 5;
                chartMap_5.put(chartCount_5, sendResult5s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void sendRate10s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            if (mysend != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = mysend - send10s;
                sendResult10s = numberFormat.format((float) rate / (float) 10);
                send10s = mysend;
                chartCount_10 += 10;
                chartMap_10.put(chartCount_10, sendResult10s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void readRate1s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            if (myread != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = myread - read1s;
                readResult1s = numberFormat.format((float) rate / (float) 1);
                read1s = myread;
                chartCountS_1++;
                chartMapS_1.put(chartCountS_1, readResult1s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/2 * * * * ? ")
    public void readRate2s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            if (myread != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = myread - read2s;
                readResult2s = numberFormat.format((float) rate / (float) 2);
                read2s = myread;
                chartCountS_2 += 2;
                chartMapS_2.put(chartCountS_2, readResult2s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/3 * * * * ? ")
    public void readRate3s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            if (myread != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = myread - read3s;
                readResult3s = numberFormat.format((float) rate / (float) 3);
                read3s = myread;
                chartCountS_3 += 3;
                chartMapS_3.put(chartCountS_3, readResult3s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ? ")
    public void readRate5s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            if (myread != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = myread - read5s;
                readResult5s = numberFormat.format((float) rate / (float) 5);
                read5s = myread;
                chartCountS_5 += 5;
                chartMapS_5.put(chartCountS_5, readResult5s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void readRate10s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            if (myread != 0) {
                // 创建一个数值格式化对象
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                long rate = myread - read10s;
                readResult10s = numberFormat.format((float) rate / (float) 10);
                read10s = myread;
                chartCountS_10 += 10;
                chartMapS_10.put(chartCountS_10, readResult10s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
