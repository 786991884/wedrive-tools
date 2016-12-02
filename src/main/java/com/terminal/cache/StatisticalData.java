package com.terminal.cache;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticalData {
    public static final long statisticalInterval = 60;
    public static long sendTotalPacketNumber = 0;
    public static long receiveTotalPacketNumber = 0;
    public static double upTotalFlow = 0;
    public static double downTotalFlow = 0;
    public static long errorTotalPacketNumber = 0;
    public static long errorTotalPacketResNumber = 0;
    public static long disconnectTotalTimes = 0;
    public static long connectTotalTimes = 0;
    public static long messageAnswerTotalTimes;
    public static long messageTotalNumber;

    public static long sendPacketNumber = 0;
    public static long receivePacketNumber = 0;
    public static double upFlow = 0;
    public static double downFlow = 0;
    public static long errorPacketNumber = 0;
    public static long errorPacketResNumber = 0;
    public static long disconnectTimes = 0;
    public static long connectTimes = 0;
    public static long messageAnswerTimes;
    public static long messageNumber;

    public static long startDate;
    public static long stageStartDate;


    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger=new AtomicInteger();
        final LocalDateTime currentPoint = LocalDateTime.now();
        //int second = currentPoint.getSecond();
        //int nano = currentPoint.getNano();
        //default format
        //System.out.println("Default format of LocalDateTime=" + currentPoint);
        //specific format
        //System.out.println(currentPoint.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS")));
        //System.out.println(currentPoint.format(DateTimeFormatter.BASIC_ISO_DATE));
        //LocalDateTime localDateTime = currentPoint.plusSeconds(1);
        //double d = Math.pow(10, 6);//10的9次方
        // 1 纳秒 = 1000皮秒 1,000 纳秒 = 1微秒 1,000,000 纳秒 = 1毫秒 1,000,000,000 纳秒 = 1秒
        //System.out.println(d);
        LocalDateTime localDateTime1 = currentPoint.plusNanos((long) (30 * Math.pow(10, 6)));
        //boolean before = currentPoint.isAfter(localDateTime1);
        //LocalDateTime now = LocalDateTime.now();
        //System.out.println(now);
        boolean b = LocalDateTime.now().isBefore(localDateTime1);
        System.out.println(b);
        while (b) {
            Thread.sleep(2);
            b = LocalDateTime.now().isBefore(localDateTime1);
        }
        //boolean isTrue = LocalDateTime.now().isBefore(localDateTime1);
        System.out.println(b);
        System.out.println(currentPoint);
        System.out.println(localDateTime1);
        //Clock clock = Clock.systemUTC();
        //System.out.println(clock.millis());
    }
}
