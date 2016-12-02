package com.simulation.terminal.cache;

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
}
