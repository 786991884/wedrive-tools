package com.terminal.cache;

import com.terminal.communication.MessageSend;
import com.terminal.netty.NettyClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;

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

    @Scheduled(cron = "0/1 * * * * ? ")
    public void sendRate1s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = mysend - send1s;
            sendResult1s = numberFormat.format((float) rate / (float) 1);
            send1s = mysend;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/2 * * * * ? ")
    public void sendRate2s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = mysend - send2s;
            sendResult2s = numberFormat.format((float) rate / (float) 2);
            send2s = mysend;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/3 * * * * ? ")
    public void sendRate3s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = mysend - send3s;
            sendResult3s = numberFormat.format((float) rate / (float) 3);
            send3s = mysend;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ? ")
    public void sendRate5s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = mysend - send5s;
            sendResult5s = numberFormat.format((float) rate / (float) 5);
            send5s = mysend;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void sendRate10s() {
        try {
            long mysend = MessageSend.sendTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = mysend - send10s;
            sendResult10s = numberFormat.format((float) rate / (float) 10);
            send10s = mysend;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void readRate1s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = myread - read1s;
            readResult1s = numberFormat.format((float) rate / (float) 1);
            read1s = myread;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/2 * * * * ? ")
    public void readRate2s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = myread - read2s;
            readResult2s = numberFormat.format((float) rate / (float) 2);
            read2s = myread;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/3 * * * * ? ")
    public void readRate3s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = myread - read3s;
            readResult3s = numberFormat.format((float) rate / (float) 3);
            read3s = myread;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/5 * * * * ? ")
    public void readRate5s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = myread - read5s;
            readResult5s = numberFormat.format((float) rate / (float) 5);
            read5s = myread;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void readRate10s() {
        try {
            long myread = NettyClientHandler.reveiveTotalPacketNum.get();
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            long rate = myread - read10s;
            readResult10s = numberFormat.format((float) rate / (float) 10);
            read10s = myread;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
