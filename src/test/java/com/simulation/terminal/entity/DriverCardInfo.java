package com.simulation.terminal.entity;

import java.text.SimpleDateFormat;

import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;

public class DriverCardInfo {
	private byte status;
	private long cardDate;
	private byte result;
	private String driverName;
	private String certificateCode;
	private String organizationName;
	private long licenseValidDate;
	
	public byte[] formatCardInfo(){
		int driverNameLen = getDriverName().length;
		int organizationNameLen = getOrganizationName().length;
		int len = 1+6+1+1+driverNameLen+20+1+organizationNameLen+4;
		byte[] data = new byte[len];
		data[0] = getStatus();
		String card = new SimpleDateFormat("yyMMddHHmmss").format(cardDate);
		ArraysUtils.arrayappend(data, 1, Convert.hexStringToBytes(card));
		data[7] = result;
		data[8] = (byte)driverNameLen;
		ArraysUtils.arrayappend(data, 9, getDriverName());
		ArraysUtils.arrayappend(data, 9+driverNameLen, getCertificateCode());
		data[29+driverNameLen] = (byte)organizationNameLen;
		ArraysUtils.arrayappend(data, 30+driverNameLen, getOrganizationName());
		String valid = new SimpleDateFormat("yyyyMMdd").format(getLicenseValidDate());
		ArraysUtils.arrayappend(data, 30+driverNameLen+organizationNameLen, Convert.hexStringToBytes(valid));
		return data;
	}
	
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public long getCardDate() {
		return cardDate;
	}
	public void setCardDate(long cardDate) {
		this.cardDate = cardDate;
	}
	public byte getResult() {
		return result;
	}
	public void setResult(byte result) {
		this.result = result;
	}
	
	public byte[] getDriverName() {
		return Convert.hexStringToBytes(Convert.mixStr2Hex(driverName));
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public byte[] getCertificateCode() {
		return Convert.hexStringToBytes(Convert.sufFillZero(Convert.mixStr2Hex(certificateCode), 20));
	}
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	public byte[] getOrganizationName() {
		return Convert.hexStringToBytes(Convert.mixStr2Hex(organizationName));
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public long getLicenseValidDate() {
		return licenseValidDate;
	}
	public void setLicenseValidDate(long licenseValidDate) {
		this.licenseValidDate = licenseValidDate;
	}
}
