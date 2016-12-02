package com.simulation.terminal.communication.protocol;

import java.util.Date;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;

public class PlatformCommonRes extends Command {
	public static Logger logger = Logger.getLogger(PlatformCommonRes.class);
	@Override
	public void processor(Packet packet) {
		byte[] serial = ArraysUtils.subarrays(packet.getMessageBody(), 0, 2);
		byte[] id = ArraysUtils.subarrays(packet.getMessageBody(), 2, 2);
		long begin = CacheManager.getSendTime(packet.getTerminalId(), Convert.byte2Int(serial, 2), Convert.byte2Int(id, 2));
		long current = new Date().getTime();
		byte result = packet.getMessageBody()[4];
		if(result == 0){
			if(Convert.byte2Int(id, 2) == 0x0003){
				CacheManager.getTerminalCache().get(packet.getTerminalId()).setCancel(true);
				logger.info(packet.getTerminalId() + ":注销成功...");
			}else if(Convert.byte2Int(id, 2) == 0x0102){
				CacheManager.getTerminalCache().get(packet.getTerminalId()).setAuth(true);
				logger.info(packet.getTerminalId() + ":鉴权成功...");
			}
		}else if(result != 4){
			StatisticalData.errorPacketResNumber++;
			StatisticalData.errorTotalPacketResNumber++;
		}
		if(begin != 0){
			StatisticalData.messageAnswerTimes += (current - begin);
			StatisticalData.messageAnswerTotalTimes += (current - begin);
			StatisticalData.messageNumber++;
			StatisticalData.messageTotalNumber++;
		}
	}
}
