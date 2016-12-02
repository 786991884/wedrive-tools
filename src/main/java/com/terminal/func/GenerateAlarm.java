package com.terminal.func;

import java.util.Random;

public class GenerateAlarm {
    public static long[] alarmType = new long[]{0x01, 0x02, 0x04, 0x08, //0~3
            0x10, 0x20, 0x40, 0x80, //4~7
            0x0100, 0x0200, 0x0400, 0x0800, //8~11
            0x1000, 0x2000, 0x4000, //12~14
            0x040000, 0x080000, //18~19
            0x01000000, 0x02000000, 0x04000000, 0x08000000, //24~27
            0x10000000, 0x20000000}; //28~29

    public static long getRandomAlarm(int alarmNumber, long defaultAlarms) {
        for (int i = 0; i < alarmNumber; i++) {
            Random random = new Random();
            defaultAlarms = defaultAlarms | alarmType[random.nextInt(alarmType.length)];
        }
        return defaultAlarms;
    }
}
