package com.terminal.communication.protocol;

import com.terminal.cache.CacheManager;
import com.terminal.cache.StatisticalData;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.TaCommand;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@LocationCommand(id = 0x8001)
public class PlatformCommonRes extends TaCommand {
    public static Logger logger = LoggerFactory.getLogger(PlatformCommonRes.class);

    @Autowired
    private CacheManager cacheManager;


    @Override
    public void processor(Packet packet) {
        byte[] serial = ArraysUtils.subarrays(packet.getMessageBody(), 0, 2);
        byte[] id = ArraysUtils.subarrays(packet.getMessageBody(), 2, 2);
        long begin = cacheManager.getSendTime(packet.getTerminalId(), Convert.byte2Int(serial, 2), Convert.byte2Int(id, 2));
        long current = new Date().getTime();
        byte result = packet.getMessageBody()[4];
        if (result == 0) {
            if (Convert.byte2Int(id, 2) == 0x0003) {
                cacheManager.getTerminalCache().get(packet.getTerminalId()).setCancel(true);
                logger.info(packet.getTerminalId() + ":注销成功...");
            } else if (Convert.byte2Int(id, 2) == 0x0102) {
                cacheManager.getTerminalCache().get(packet.getTerminalId()).setAuth(true);
                logger.info(packet.getTerminalId() + ":鉴权成功...");
            }
        } else if (result != 4) {
            StatisticalData.errorPacketResNumber++;
            StatisticalData.errorTotalPacketResNumber++;
        }
        if (begin != 0) {
            StatisticalData.messageAnswerTimes += (current - begin);
            StatisticalData.messageAnswerTotalTimes += (current - begin);
            StatisticalData.messageNumber++;
            StatisticalData.messageTotalNumber++;
        }
    }
}
