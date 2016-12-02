package com.terminal.entity;


import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;

import java.text.SimpleDateFormat;
import java.util.Random;

public class LocationData {
    private long alarm;
    private long status;
    private int latitude;
    private int longitude;
    private int altitude;
    private int speed;
    private int direction;
    private long gpsDate;
    private long mileage;
    private byte[] statusAddition;
    private byte[] alarmAddition;
    private byte[] fault;


    public byte[] getStatusAddition() {
        this.statusAddition = new byte[146];
        Random random = new Random();
        ArraysUtils.arrayappend(statusAddition, 0, new byte[]{13});
        ArraysUtils.arrayappend(statusAddition, 1, new byte[]{0});
//		1	0x18FEF600	Byte1	颗粒捕集器进气压力(0.5kPa/bit)
//			0x18FEF600	Byte2	相对增压压力(2kPa/bit)
//			0x18FEF600	Byte4	绝对增压压力(2kPa/bit)
//			0X00FEF600	Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
        ArraysUtils.arrayappend(statusAddition, 2, Convert.hexStringToBytes("18FEF600"));
//		ArraysUtils.arrayappend(statusAddition, 6, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),0,(byte)random.nextInt(256),0,(byte)random.nextInt(256),(byte)random.nextInt(2),0});
        ArraysUtils.arrayappend(statusAddition, 6, new byte[]{(byte) (100 + random.nextInt(5)), (byte) (100 + random.nextInt(5)), 0, (byte) (100 + random.nextInt(5)), 0, (byte) (100 + random.nextInt(5)), (byte) (40 + random.nextInt(5)), 0});
//		2	0X18FEF500	Byte6	进气温度(1℃/bit,Offset:-40℃)
//			0X18FEF500	Byte1	大气压力(0.5kPa/bit,Offset:0hPa)
//			0X18FEF500	Byte2-3	发动机舱内部温度(Not realized，Set 0xFFFF)
//			0X18FEF500	Byte4-5	大气温度(0.03125℃/bit,Offset:-273℃)
//			0X18FEF500	Byte7-8	路面温度(Not realized，Set 0xFFFF)
        ArraysUtils.arrayappend(statusAddition, 14, Convert.hexStringToBytes("18FEF500"));
//		ArraysUtils.arrayappend(statusAddition, 18, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(2),(byte)random.nextInt(256),(byte)random.nextInt(2),(byte)random.nextInt(120),(byte)random.nextInt(120),0});
        ArraysUtils.arrayappend(statusAddition, 18, new byte[]{(byte) (100 + random.nextInt(5)), (byte) (60 + random.nextInt(5)), 0, (byte) (100 + random.nextInt(5)), (byte) (40 + random.nextInt(5)), (byte) (60 + random.nextInt(5)), (byte) (30 + random.nextInt(5)), 0});
//		3	0x18FEE400	Byte4	冷启动灯（1表示正常，0表示异常）
        ArraysUtils.arrayappend(statusAddition, 26, Convert.hexStringToBytes("18FEE400"));
        ArraysUtils.arrayappend(statusAddition, 30, new byte[]{0, 0, 0, (byte) random.nextInt(2), 0, 0, 0, 0});
//		ArraysUtils.arrayappend(statusAddition, 30, new byte[]{0,0,0,1,0,0,0,0});
//		4	0x18FEE000	Byte5-8	整车里程（0.125Km/bit）
        ArraysUtils.arrayappend(statusAddition, 38, Convert.hexStringToBytes("18FEE000"));

        ArraysUtils.arrayappend(statusAddition, 42, new byte[]{0, 0, 0, 0, (byte) random.nextInt(256), (byte) random.nextInt(256), (byte) random.nextInt(256), (byte) random.nextInt(256)});
        //ArraysUtils.arrayappend(statusAddition, 42, ArraysUtils.arraycopy(new byte[]{0, 0, 0, 0}, cacheManager.getCanMileage()));

//		5	0x18FEFF00	Byte1Bit1-2	油中有水指示（1表示正常，0表示异常）
        ArraysUtils.arrayappend(statusAddition, 50, Convert.hexStringToBytes("18FEFF00"));
        ArraysUtils.arrayappend(statusAddition, 54, new byte[]{(byte) random.nextInt(2), 0, 0, 0, 0, 0, 0, 0});
//		ArraysUtils.arrayappend(statusAddition, 54, new byte[]{1,0,0,0,0,0,0,0});
//		6	0x18FEE900	Byte5-8	车辆当前油量（0.5 L/bit,）
        ArraysUtils.arrayappend(statusAddition, 62, Convert.hexStringToBytes("18FEE900"));
//		ArraysUtils.arrayappend(statusAddition, 66, new byte[]{0,0,0,0,(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(2)});
        ArraysUtils.arrayappend(statusAddition, 66, new byte[]{0, 0, 0, 0, (byte) (100 + random.nextInt(5)), 0, 0, 0});
//		7	0x0CF00400	Byte4-5	车辆当前转速(0.125rpm/bit)
        ArraysUtils.arrayappend(statusAddition, 74, Convert.hexStringToBytes("0CF00400"));
//		ArraysUtils.arrayappend(statusAddition, 78, new byte[]{0,0,0,(byte)random.nextInt(256),(byte)random.nextInt(256),0,0,0});
        ArraysUtils.arrayappend(statusAddition, 78, new byte[]{0, 0, 0, (byte) (100 + random.nextInt(5)), (byte) (70 + random.nextInt(3)), 0, 0, 0});
//		8	0x18FEEF00	Byte 1	燃油压力（4Kpa/bit）
//			0x18FEEF00	Byte 3	机油液位（0.4%/bit）
//			0x18FEEF00	Byte 4	机油压力（4Kpa/bit）
//			0x18FEEF00	Byte 8	冷却液液位（0.4%/bit）
        ArraysUtils.arrayappend(statusAddition, 86, Convert.hexStringToBytes("18FEEF00"));
//		ArraysUtils.arrayappend(statusAddition, 90, new byte[]{(byte)random.nextInt(256),0,(byte)random.nextInt(256),(byte)random.nextInt(256),0,0,0,(byte)random.nextInt(256)});
        ArraysUtils.arrayappend(statusAddition, 90, new byte[]{(byte) (random.nextInt(5)), 0, (byte) (200 + random.nextInt(5)), (byte) (random.nextInt(5)), 0, 0, 0, (byte) (200 + random.nextInt(5))});
//		9	0x18FEEE00	Byte 1	发动机冷却水温度（1℃/bit，Offset:-40℃）
//			0x18FEEE00	Byte 2	燃油温度（1℃/bit，Offset:-40℃）
//			0x18FEEE00	Byte3-4	机油温度（0.03125℃/bit，Offset:-273℃）
        ArraysUtils.arrayappend(statusAddition, 98, Convert.hexStringToBytes("18FEEE00"));
//		ArraysUtils.arrayappend(statusAddition, 102, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),0,0,0,0});
        ArraysUtils.arrayappend(statusAddition, 102, new byte[]{(byte) (130 + random.nextInt(5)), (byte) (60 + random.nextInt(5)), (byte) (100 + random.nextInt(5)), (byte) (40 + random.nextInt(5)), 0, 0, 0, 0});
//		10	0x18FE5600	Byte 1	尿素箱液位（0.4%/bit，Offset:0）
//			0x18FE5600	Byte 2	尿素箱温度（1℃/bit，Offset:-40℃）
        ArraysUtils.arrayappend(statusAddition, 110, Convert.hexStringToBytes("18FE5600"));
//		ArraysUtils.arrayappend(statusAddition, 114, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),0,0,0,0,0,0});
        ArraysUtils.arrayappend(statusAddition, 114, new byte[]{(byte) (200 + random.nextInt(5)), (byte) (70 + random.nextInt(5)), 0, 0, 0, 0, 0, 0});
//		11	0x18FEE500	Byte1-4	发动机累计运行时间（0.05h/bit）
//			0x18FEE500	Byte5-8	发动机累计转数（1000rpm/bit）
        ArraysUtils.arrayappend(statusAddition, 122, Convert.hexStringToBytes("18FEE500"));
//		ArraysUtils.arrayappend(statusAddition, 126, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256),(byte)random.nextInt(256)});
        ArraysUtils.arrayappend(statusAddition, 126, new byte[]{0, (byte) random.nextInt(50), 1, 0, 0, (byte) (80 + random.nextInt(5)), 0, 0});
//		12	0x18FEF200	Byte1-2	发动机燃油消耗率（0.05L/h/bit）
//			0x18FEF200	Byte5-6	平均燃油消耗率（0.001953125Km/L/bit）
        ArraysUtils.arrayappend(statusAddition, 134, Convert.hexStringToBytes("18FEF200"));
//		ArraysUtils.arrayappend(statusAddition, 138, new byte[]{(byte)random.nextInt(256),(byte)random.nextInt(256),0,0,(byte)random.nextInt(256),(byte)random.nextInt(256),0,0});
        ArraysUtils.arrayappend(statusAddition, 138, new byte[]{(byte) (100 + random.nextInt(5)), 0, 0, 0, (byte) random.nextInt(20), 20, 0, 0});
        return statusAddition;
    }


    public byte[] getAlarmAddition() {
//		Random random = new Random();
//		if(random.nextInt(2)==0){
//			this.alarmAddition = new byte[]{0,(byte)(0x40),0,0,0,(byte)(0xFF),(byte)(0xFF),(byte)(0xFF)};
//		}else{
//			this.alarmAddition = new byte[]{0,0,0,0,0,0,0,0};
//		}
        this.alarmAddition = new byte[]{0, (byte) (0x40), 0, 0, 0, (byte) (0xFF), (byte) (0xFF), (byte) (0xFF)};
        return this.alarmAddition;
    }

    public byte[] formatLocationData() {
//		byte[] data = new byte[44];
        byte[] data = new byte[38 + getStatusAddition().length + getCanFault().length];
        ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
        ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
        ArraysUtils.arrayappend(data, 8, Convert.longTobytes(latitude, 4));
        ArraysUtils.arrayappend(data, 12, Convert.longTobytes(longitude, 4));
        ArraysUtils.arrayappend(data, 16, Convert.longTobytes(altitude, 2));
        ArraysUtils.arrayappend(data, 18, Convert.longTobytes(speed, 2));
        ArraysUtils.arrayappend(data, 20, Convert.longTobytes(direction, 2));
        String dd = new SimpleDateFormat("yyMMddHHmmss").format(gpsDate);
        ArraysUtils.arrayappend(data, 22, Convert.hexStringToBytes(dd));
        data[28] = 0x01;
        data[29] = 0x04;
        ArraysUtils.arrayappend(data, 30, Convert.longTobytes(mileage, 4));
//		data[34] = (byte)0xEF;
//		data[35] = 0x08;
//		ArraysUtils.arrayappend(data, 36, getAlarmAddition());
        data[34] = 0x74;
        data[35] = (byte) getStatusAddition().length;
        ArraysUtils.arrayappend(data, 36, getStatusAddition());
        data[36 + getStatusAddition().length] = (byte) 0x83;
        data[37 + getStatusAddition().length] = (byte) getCanFault().length;
        ArraysUtils.arrayappend(data, 38 + getStatusAddition().length, getCanFault());
        return data;
    }

    public byte[] getCanFault() {
        this.fault = new byte[7];
        ArraysUtils.arrayappend(fault, 0, new byte[]{2});
        ArraysUtils.arrayappend(fault, 1, new byte[]{100, 0, 1});
        ArraysUtils.arrayappend(fault, 4, new byte[]{100, 0, 5});
        return fault;
    }

    public byte[] formatErrorLocationData() {
        byte[] data = null;
        Random random = new Random();
        int type = random.nextInt(5);
        switch (type) {
            //未来时间
            case 0: {
                data = new byte[34];
                ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
                ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
                ArraysUtils.arrayappend(data, 8, Convert.longTobytes(latitude, 4));
                ArraysUtils.arrayappend(data, 12, Convert.longTobytes(longitude, 4));
                ArraysUtils.arrayappend(data, 16, Convert.longTobytes(altitude, 2));
                ArraysUtils.arrayappend(data, 18, Convert.longTobytes(speed, 2));
                ArraysUtils.arrayappend(data, 20, Convert.longTobytes(direction, 2));
                String dd = new SimpleDateFormat("yyMMddHHmmss").format(gpsDate + 864000000);
                ArraysUtils.arrayappend(data, 22, Convert.hexStringToBytes(dd));
                data[28] = 0x01;
                data[29] = 0x04;
                ArraysUtils.arrayappend(data, 30, Convert.longTobytes(mileage, 4));
            }
            //经纬度为0
            case 1: {
                data = new byte[34];
                ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
                ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
                ArraysUtils.arrayappend(data, 8, Convert.longTobytes(0, 4));
                ArraysUtils.arrayappend(data, 12, Convert.longTobytes(0, 4));
                ArraysUtils.arrayappend(data, 16, Convert.longTobytes(altitude, 2));
                ArraysUtils.arrayappend(data, 18, Convert.longTobytes(speed, 2));
                ArraysUtils.arrayappend(data, 20, Convert.longTobytes(direction, 2));
                String dd = new SimpleDateFormat("yyMMddHHmmss").format(gpsDate);
                ArraysUtils.arrayappend(data, 22, Convert.hexStringToBytes(dd));
                data[28] = 0x01;
                data[29] = 0x04;
                ArraysUtils.arrayappend(data, 30, Convert.longTobytes(mileage, 4));
            }
            //高度、速度、方向有问题
            case 2: {
                data = new byte[34];
                ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
                ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
                ArraysUtils.arrayappend(data, 8, Convert.longTobytes(latitude, 4));
                ArraysUtils.arrayappend(data, 12, Convert.longTobytes(longitude, 4));
                ArraysUtils.arrayappend(data, 16, Convert.longTobytes(0xffff, 2));
                ArraysUtils.arrayappend(data, 18, Convert.longTobytes(0xffff, 2));
                ArraysUtils.arrayappend(data, 20, Convert.longTobytes(0xffff, 2));
                String dd = new SimpleDateFormat("yyMMddHHmmss").format(gpsDate);
                ArraysUtils.arrayappend(data, 22, Convert.hexStringToBytes(dd));
                data[28] = 0x01;
                data[29] = 0x04;
                ArraysUtils.arrayappend(data, 30, Convert.longTobytes(mileage, 4));
            }
            //缺失经纬度
            case 3: {
                data = new byte[20];
                ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
                ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
                ArraysUtils.arrayappend(data, 8, Convert.longTobytes(altitude, 2));
                ArraysUtils.arrayappend(data, 10, Convert.longTobytes(speed, 2));
                ArraysUtils.arrayappend(data, 12, Convert.longTobytes(direction, 2));
                String dd = new SimpleDateFormat("yyMMddHHmmss").format(gpsDate - 864000000);
                ArraysUtils.arrayappend(data, 14, Convert.hexStringToBytes(dd));
            }
            //数据缺失高度、速度、方向、时间
            case 4: {
                data = new byte[16];
                ArraysUtils.arrayappend(data, 0, Convert.longTobytes(alarm, 4));
                ArraysUtils.arrayappend(data, 4, Convert.longTobytes(status, 4));
                ArraysUtils.arrayappend(data, 8, Convert.longTobytes(latitude, 4));
                ArraysUtils.arrayappend(data, 12, Convert.longTobytes(longitude, 4));
            }
        }
        return data;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public long getAlarm() {
        return alarm;
    }

    public void setAlarm(long alarm) {
        this.alarm = alarm;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public long getGpsDate() {
        return gpsDate;
    }

    public void setGpsDate(long gpsDate) {
        this.gpsDate = gpsDate;
    }
}
