package com.simulation.terminal.util;

public class ServerConfig {
	public static String serverTcpIP;
	public static int serverTcpPort;
	public static int terminalNumber;
	public static long terminalIdentifyStart;
	public static int gpsDataLatitude;
	public static int gpsDataLongitude;
	public static int gpsDataLatitudeIncrease;
	public static int gpsDataLongitudeIncrease;
	public static int gpsDataAltitudeMax;
	public static int gpsDataSpeedMax;
	public static long gpsDataStatusDefault;
	public static int statusAccSwitchPercentage;
	public static int gpsDataAlarmNumber;
	public static int terminalAlarmPercentage;
	public static int terminalErrorDataPercentage;
	public static int terminalLinkDisconnectPercentage;
	public static int terminalContinue;
	public static int terminalBatchNumber;
	public static int terminalBatchInterval;
	
	public static String vehicleLicensePre;
	public static int vehicleLicenseSufLenth;
	public static int vehicleColor;
	public static int provinceCode;
	public static int cityCode;
	public static String terminalManufacturer;
	public static String terminalType;
	public static String terminalNoPro;
	public static int terminalNoStart;
	public static int terminalNoSufLength;
	
	public static int terminalCancel;
	public static long gpsDataAlarmsDefault;
	
	static{
		serverTcpIP = PropertiesUtil.getValue("server.tcp.ip");
		serverTcpPort = Integer.parseInt(PropertiesUtil.getValue("server.tcp.port"));
		terminalNumber = Integer.parseInt(PropertiesUtil.getValue("terminal.number"));
		terminalIdentifyStart = Long.parseLong(PropertiesUtil.getValue("terminal.identify.start"));
		gpsDataLatitude = Integer.parseInt(PropertiesUtil.getValue("gps.data.latitude"));
		gpsDataLongitude = Integer.parseInt(PropertiesUtil.getValue("gps.data.longitude"));
		gpsDataLatitudeIncrease = Integer.parseInt(PropertiesUtil.getValue("gps.data.latitude.increase"));
		gpsDataLongitudeIncrease = Integer.parseInt(PropertiesUtil.getValue("gps.data.longitude.increase"));
		gpsDataAltitudeMax = Integer.parseInt(PropertiesUtil.getValue("gps.data.altitude.max"));
		gpsDataSpeedMax = Integer.parseInt(PropertiesUtil.getValue("gps.data.speed.max"));
		gpsDataStatusDefault = Long.parseLong(PropertiesUtil.getValue("gps.data.status.default"));
		gpsDataAlarmNumber = Integer.parseInt(PropertiesUtil.getValue("gps.data.alarm.number"));
		terminalAlarmPercentage = Integer.parseInt(PropertiesUtil.getValue("terminal.alarm.percentage"));
		terminalErrorDataPercentage = Integer.parseInt(PropertiesUtil.getValue("terminal.error.data.percentage"));
		terminalLinkDisconnectPercentage = Integer.parseInt(PropertiesUtil.getValue("terminal.link.disconnect.percentage"));
		terminalContinue = Integer.parseInt(PropertiesUtil.getValue("terminal.continue"));
		terminalBatchNumber = Integer.parseInt(PropertiesUtil.getValue("terminal.batch.number"));
		terminalBatchInterval = Integer.parseInt(PropertiesUtil.getValue("terminal.batch.interval"));
		vehicleLicensePre = PropertiesUtil.getValue("vehicle.license.pre");
		vehicleLicenseSufLenth = Integer.parseInt(PropertiesUtil.getValue("vehicle.license.suf.length"));
		vehicleColor = Integer.parseInt(PropertiesUtil.getValue("vehicle.color"));
		provinceCode = Integer.parseInt(PropertiesUtil.getValue("province.code"));
		cityCode = Integer.parseInt(PropertiesUtil.getValue("city.code"));
		terminalManufacturer = PropertiesUtil.getValue("terminal.manufacturer");
		terminalType = PropertiesUtil.getValue("terminal.type");
		terminalNoPro = PropertiesUtil.getValue("terminal.no.pro");
		terminalNoStart = Integer.parseInt(PropertiesUtil.getValue("terminal.no.start"));
		terminalNoSufLength = Integer.parseInt(PropertiesUtil.getValue("terminal.no.suf.length"));
		terminalCancel = Integer.parseInt(PropertiesUtil.getValue("terminal.cancel"));
		gpsDataAlarmsDefault = Long.parseLong(PropertiesUtil.getValue("gps.data.alarms.default"));
		statusAccSwitchPercentage = Integer.parseInt(PropertiesUtil.getValue("status.acc.switch.percentage"));
	}
}
