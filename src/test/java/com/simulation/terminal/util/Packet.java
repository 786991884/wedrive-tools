package com.simulation.terminal.util;

public class Packet {
	private long terminalId;
	private int messageId;
	private byte[] messageBody;
	private int serialNumber;
	
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public byte[] getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
	}
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	
}
