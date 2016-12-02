package com.simulation.terminal.entity;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

public class TerminalStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private SocketChannel channel;
	private boolean isChannelActive;
	private long alarm;
	private String terminalAuthCode;
	private boolean isAuth;
	private boolean isAlarm;
	private long beginData;
	private boolean isError;
	private boolean isDisconnect;
	private boolean isCancel;
	private long accStatus;
	private boolean channelIsConnecting;
	
	public boolean isChannelIsConnecting() {
		return channelIsConnecting;
	}
	public void setChannelIsConnecting(boolean channelIsConnecting) {
		this.channelIsConnecting = channelIsConnecting;
	}
	
	public long getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(long accStatus) {
		this.accStatus = accStatus;
	}
	public boolean isCancel() {
		return isCancel;
	}
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	public boolean isAuth() {
		return isAuth;
	}
	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}
	
	public SocketChannel getChannel() {
		return channel;
	}
	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
	public long getAlarm() {
		return alarm;
	}
	public void setAlarm(long alarm) {
		this.alarm = alarm;
	}
	public String getTerminalAuthCode() {
		return terminalAuthCode;
	}
	public void setTerminalAuthCode(String terminalAuthCode) {
		this.terminalAuthCode = terminalAuthCode;
	}
	public boolean isAlarm() {
		return isAlarm;
	}
	public void setAlarm(boolean isAlarm) {
		this.isAlarm = isAlarm;
	}
	public long getBeginData() {
		return beginData;
	}
	public void setBeginData(long beginData) {
		this.beginData = beginData;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public boolean isDisconnect() {
		return isDisconnect;
	}
	public void setDisconnect(boolean isDisconnect) {
		this.isDisconnect = isDisconnect;
	}
	public boolean isChannelActive() {
		return isChannelActive;
	}
	public void setChannelActive(boolean isChannelActive) {
		this.isChannelActive = isChannelActive;
	}
}
