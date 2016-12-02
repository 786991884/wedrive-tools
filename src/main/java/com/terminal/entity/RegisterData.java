package com.terminal.entity;


import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;

public class RegisterData {
    private int provinceId;
    private int cityId;
    private String manufacturerId;
    private String terminalType;
    private String terminalIdentify;
    private int vehicleColor;
    private String vehicleLicense;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalIdentify() {
        return terminalIdentify;
    }

    public void setTerminalIdentify(String terminalIdentify) {
        this.terminalIdentify = terminalIdentify;
    }

    public int getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(int vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    public byte[] formatRegisterData() {
        byte[] data = new byte[37];
        ArraysUtils.arrayappend(data, 0, Convert.longTobytes(provinceId, 2));
        ArraysUtils.arrayappend(data, 2, Convert.longTobytes(cityId, 2));
        ArraysUtils.arrayappend(data, 4, Convert.hexStringToBytes(Convert.preFillZero(Convert.mixStr2Hex(manufacturerId), 10)));
        ArraysUtils.arrayappend(data, 9, Convert.hexStringToBytes(Convert.sufFillZero(Convert.mixStr2Hex(terminalType), 40)));
        ArraysUtils.arrayappend(data, 29, Convert.hexStringToBytes(Convert.sufFillZero(Convert.mixStr2Hex(terminalIdentify), 14)));
        data[36] = (byte) vehicleColor;
        byte[] license = Convert.hexStringToBytes(Convert.mixStr2Hex(vehicleLicense));
        byte[] register = ArraysUtils.arraycopy(data, license);
        return register;
    }
}
