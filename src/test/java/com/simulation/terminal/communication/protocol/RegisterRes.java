package com.simulation.terminal.communication.protocol;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.util.ArraysUtils;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.Packet;

public class RegisterRes extends Command {
	public static Logger logger = Logger.getLogger(RegisterRes.class);
	@Override
	public void processor(Packet packet) {
		byte[] serial = ArraysUtils.subarrays(packet.getMessageBody(), 0, 2);
		long begin = CacheManager.getSendTime(packet.getTerminalId(), Convert.byte2Int(serial, 2), 0x0100);
		long current = new Date().getTime();
		byte result = ArraysUtils.subarrays(packet.getMessageBody(), 2, 1)[0];
		if(result == 0){
			String auth;
			try {
				auth = new String((ArraysUtils.subarrays(packet.getMessageBody(), 3)), "GBK");
				CacheManager.getAuthCode().put(packet.getTerminalId(), auth);
				TerminalStatus status = CacheManager.getTerminalCache().get(packet.getTerminalId());
				if(status != null){
					status.setTerminalAuthCode(auth);
					logger.info(packet.getTerminalId() + ":注册成功.鉴权码为：" + auth);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			logger.info(packet.getTerminalId() + ":注册失败...");
		}
		if(begin != 0){
			StatisticalData.messageAnswerTimes += (current - begin);
			StatisticalData.messageAnswerTotalTimes += (current - begin);
			StatisticalData.messageNumber++;
			StatisticalData.messageTotalNumber++;
		}
	}
}
