package com.terminal.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigUtils {
    @Value("${server.tcp.ip:192.168.85.38}")
    private String serverTcpIP;
    @Value("${server.tcp.port:30503}")
    private int serverTcpPort;
    @Value("${terminal.number:1}")
    private int terminalNumber;
    @Value("${terminal.identify.start:18274000005}")
    private long terminalIdentifyStart;
    @Value("${vehicle.license.pre:CB}")
    private String vehicleLicensePre;
    @Value("${vehicle.license.suf.length:5}")
    private int vehicleLicenseSufLength;
    @Value("${vehicle.color:2}")
    private int vehicleColor;
    @Value("${province.code:34}")
    private int provinceCode;
    @Value("${city.code:0100}")
    private int cityCode;
    @Value("${terminal.manufacturer:10100}")
    private String terminalManufacturer;
    @Value("${terminal.type:GK-110R6-GC}")
    private String terminalType;
    @Value("${terminal.no.pro:Q}")
    private String terminalNoPro;
    @Value("${terminal.no.start:000005}")
    private int terminalNoStart;
    @Value("${terminal.no.suf.length:6}")
    private int terminalNoSufLength;
    @Value("${gps.data.latitude:29612223}")
    private int gpsDataLatitude;
    @Value("${gps.data.longitude:113916470}")
    private int gpsDataLongitude;
    @Value("${gps.data.latitude.increase:10}")
    private int gpsDataLatitudeIncrease;
    @Value("${gps.data.longitude.increase:10}")
    private int gpsDataLongitudeIncrease;
    @Value("${gps.data.altitude.max:1000}")
    private int gpsDataAltitudeMax;
    @Value("${gps.data.speed.max:1000}")
    private int gpsDataSpeedMax;
    @Value("${gps.data.status.default:262146}")
    private long gpsDataStatusDefault;
    @Value("${status.acc.switch.percentage:50}")
    private int statusAccSwitchPercentage;
    @Value("${gps.data.alarms.default:0}")
    private long gpsDataAlarmsDefault;
    //random produce alarm number
    @Value("${gps.data.alarm.number:0}")
    private int gpsDataAlarmNumber;
    //produce alarm, have error data, disconnect percentage
    @Value("${terminal.alarm.percentage:0}")
    private int terminalAlarmPercentage;
    @Value("${terminal.error.data.percentage:0}")
    private int terminalErrorDataPercentage;
    @Value("${terminal.link.disconnect.percentage:0}")
    private int terminalLinkDisconnectPercentage;
    //alarm,error data,disconnect happen continue time
    @Value("${terminal.continue:3600}")
    private int terminalContinue;
    //batch com.terminal location data number, each batch interval
    @Value("${terminal.batch.number:1}")
    private int terminalBatchNumber;
    @Value("${terminal.batch.interval:5000}")
    private int terminalBatchInterval;
    //com.terminal connect first send cancel command ? 0:no ; 1:yes
    @Value("${terminal.cancel:0}")
    private int terminalCancel;
    @Value("${vehicle.license.sufLenth:0}")
    public int vehicleLicenseSufLenth;
    @Value("${cache.auth.data.path}")
    private String authPath;

    @Value("${terminal.auth}")
    private boolean terminalAuth;
    @Value("${terminal.location.data}")
    private boolean terminalLocationData;


    @Value("${command.expireTime.5:5}")
    private int expireTime_5;
    @Value("${command.expireTime.10:10}")
    private int expireTime_10;
    @Value("${command.expireTime.15:15}")
    private int expireTime_15;
    @Value("${command.expireTime:30}")
    private int expireTime;
    @Value("${pressure.time.minute:30}")
    private int pressureTime;


    public boolean isTerminalLocationData() {
        return terminalLocationData;
    }

    public void setTerminalLocationData(boolean terminalLocationData) {
        this.terminalLocationData = terminalLocationData;
    }
    public int getPressureTime() {
        return pressureTime;
    }

    public void setPressureTime(int pressureTime) {
        this.pressureTime = pressureTime;
    }

    public int getExpireTime_5() {
        return expireTime_5;
    }

    public void setExpireTime_5(int expireTime_5) {
        this.expireTime_5 = expireTime_5;
    }

    public int getExpireTime_10() {
        return expireTime_10;
    }

    public void setExpireTime_10(int expireTime_10) {
        this.expireTime_10 = expireTime_10;
    }

    public int getExpireTime_15() {
        return expireTime_15;
    }

    public void setExpireTime_15(int expireTime_15) {
        this.expireTime_15 = expireTime_15;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isTerminalAuth() {
        return terminalAuth;
    }

    public void setTerminalAuth(boolean terminalAuth) {
        this.terminalAuth = terminalAuth;
    }

    public String getServerTcpIP() {
        return serverTcpIP;
    }

    public void setServerTcpIP(String serverTcpIP) {
        this.serverTcpIP = serverTcpIP;
    }

    public int getServerTcpPort() {
        return serverTcpPort;
    }

    public void setServerTcpPort(int serverTcpPort) {
        this.serverTcpPort = serverTcpPort;
    }

    public int getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(int terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public long getTerminalIdentifyStart() {
        return terminalIdentifyStart;
    }

    public void setTerminalIdentifyStart(long terminalIdentifyStart) {
        this.terminalIdentifyStart = terminalIdentifyStart;
    }

    public String getVehicleLicensePre() {
        return vehicleLicensePre;
    }

    public void setVehicleLicensePre(String vehicleLicensePre) {
        this.vehicleLicensePre = vehicleLicensePre;
    }

    public int getVehicleLicenseSufLength() {
        return vehicleLicenseSufLength;
    }

    public void setVehicleLicenseSufLength(int vehicleLicenseSufLength) {
        this.vehicleLicenseSufLength = vehicleLicenseSufLength;
    }

    public int getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(int vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getTerminalManufacturer() {
        return terminalManufacturer;
    }

    public void setTerminalManufacturer(String terminalManufacturer) {
        this.terminalManufacturer = terminalManufacturer;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalNoPro() {
        return terminalNoPro;
    }

    public void setTerminalNoPro(String terminalNoPro) {
        this.terminalNoPro = terminalNoPro;
    }

    public int getTerminalNoStart() {
        return terminalNoStart;
    }

    public void setTerminalNoStart(int terminalNoStart) {
        this.terminalNoStart = terminalNoStart;
    }

    public int getTerminalNoSufLength() {
        return terminalNoSufLength;
    }

    public void setTerminalNoSufLength(int terminalNoSufLength) {
        this.terminalNoSufLength = terminalNoSufLength;
    }

    public int getGpsDataLatitude() {
        return gpsDataLatitude;
    }

    public void setGpsDataLatitude(int gpsDataLatitude) {
        this.gpsDataLatitude = gpsDataLatitude;
    }

    public int getGpsDataLongitude() {
        return gpsDataLongitude;
    }

    public void setGpsDataLongitude(int gpsDataLongitude) {
        this.gpsDataLongitude = gpsDataLongitude;
    }

    public int getGpsDataLatitudeIncrease() {
        return gpsDataLatitudeIncrease;
    }

    public void setGpsDataLatitudeIncrease(int gpsDataLatitudeIncrease) {
        this.gpsDataLatitudeIncrease = gpsDataLatitudeIncrease;
    }

    public int getGpsDataLongitudeIncrease() {
        return gpsDataLongitudeIncrease;
    }

    public void setGpsDataLongitudeIncrease(int gpsDataLongitudeIncrease) {
        this.gpsDataLongitudeIncrease = gpsDataLongitudeIncrease;
    }

    public int getGpsDataAltitudeMax() {
        return gpsDataAltitudeMax;
    }

    public void setGpsDataAltitudeMax(int gpsDataAltitudeMax) {
        this.gpsDataAltitudeMax = gpsDataAltitudeMax;
    }

    public int getGpsDataSpeedMax() {
        return gpsDataSpeedMax;
    }

    public void setGpsDataSpeedMax(int gpsDataSpeedMax) {
        this.gpsDataSpeedMax = gpsDataSpeedMax;
    }

    public long getGpsDataStatusDefault() {
        return gpsDataStatusDefault;
    }

    public void setGpsDataStatusDefault(long gpsDataStatusDefault) {
        this.gpsDataStatusDefault = gpsDataStatusDefault;
    }

    public int getStatusAccSwitchPercentage() {
        return statusAccSwitchPercentage;
    }

    public void setStatusAccSwitchPercentage(int statusAccSwitchPercentage) {
        this.statusAccSwitchPercentage = statusAccSwitchPercentage;
    }

    public long getGpsDataAlarmsDefault() {
        return gpsDataAlarmsDefault;
    }

    public void setGpsDataAlarmsDefault(long gpsDataAlarmsDefault) {
        this.gpsDataAlarmsDefault = gpsDataAlarmsDefault;
    }

    public int getGpsDataAlarmNumber() {
        return gpsDataAlarmNumber;
    }

    public void setGpsDataAlarmNumber(int gpsDataAlarmNumber) {
        this.gpsDataAlarmNumber = gpsDataAlarmNumber;
    }

    public int getTerminalAlarmPercentage() {
        return terminalAlarmPercentage;
    }

    public void setTerminalAlarmPercentage(int terminalAlarmPercentage) {
        this.terminalAlarmPercentage = terminalAlarmPercentage;
    }

    public int getTerminalErrorDataPercentage() {
        return terminalErrorDataPercentage;
    }

    public void setTerminalErrorDataPercentage(int terminalErrorDataPercentage) {
        this.terminalErrorDataPercentage = terminalErrorDataPercentage;
    }

    public int getTerminalLinkDisconnectPercentage() {
        return terminalLinkDisconnectPercentage;
    }

    public void setTerminalLinkDisconnectPercentage(int terminalLinkDisconnectPercentage) {
        this.terminalLinkDisconnectPercentage = terminalLinkDisconnectPercentage;
    }

    public int getTerminalContinue() {
        return terminalContinue;
    }

    public void setTerminalContinue(int terminalContinue) {
        this.terminalContinue = terminalContinue;
    }

    public int getTerminalBatchNumber() {
        return terminalBatchNumber;
    }

    public void setTerminalBatchNumber(int terminalBatchNumber) {
        this.terminalBatchNumber = terminalBatchNumber;
    }

    public int getTerminalBatchInterval() {
        return terminalBatchInterval;
    }

    public void setTerminalBatchInterval(int terminalBatchInterval) {
        this.terminalBatchInterval = terminalBatchInterval;
    }

    public int getTerminalCancel() {
        return terminalCancel;
    }

    public void setTerminalCancel(int terminalCancel) {
        this.terminalCancel = terminalCancel;
    }

    public int getVehicleLicenseSufLenth() {
        return vehicleLicenseSufLenth;
    }

    public void setVehicleLicenseSufLenth(int vehicleLicenseSufLenth) {
        this.vehicleLicenseSufLenth = vehicleLicenseSufLenth;
    }

    public String getAuthPath() {
        return authPath;
    }

    public void setAuthPath(String authPath) {
        this.authPath = authPath;
    }
}