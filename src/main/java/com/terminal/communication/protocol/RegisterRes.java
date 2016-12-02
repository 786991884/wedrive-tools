package com.terminal.communication.protocol;

import com.terminal.cache.CacheManager;
import com.terminal.cache.StatisticalData;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.TaCommand;
import com.terminal.entity.TerminalStatus;
import com.terminal.util.ArraysUtils;
import com.terminal.util.Convert;
import com.terminal.util.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@LocationCommand(id = 0x8100)
public class RegisterRes extends TaCommand {
    public static Logger logger = LoggerFactory.getLogger(RegisterRes.class);

    @Autowired
    private CacheManager cacheManager;


    @Override
    public void processor(Packet packet) {
        byte[] serial = ArraysUtils.subarrays(packet.getMessageBody(), 0, 2);
        long begin = cacheManager.getSendTime(packet.getTerminalId(), Convert.byte2Int(serial, 2), 0x0100);
        long current = new Date().getTime();
        byte result = ArraysUtils.subarrays(packet.getMessageBody(), 2, 1)[0];
        if (result == 0) {
            String auth;
            try {
                auth = new String((ArraysUtils.subarrays(packet.getMessageBody(), 3)), "GBK");
                cacheManager.getAuthCode().put(packet.getTerminalId(), auth);
                TerminalStatus status = cacheManager.getTerminalCache().get(packet.getTerminalId());
                if (status != null) {
                    status.setTerminalAuthCode(auth);
                    logger.info(packet.getTerminalId() + ":注册成功.鉴权码为：" + auth);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            logger.info(packet.getTerminalId() + ":注册失败...");
        }
        if (begin != 0) {
            StatisticalData.messageAnswerTimes += (current - begin);
            StatisticalData.messageAnswerTotalTimes += (current - begin);
            StatisticalData.messageNumber++;
            StatisticalData.messageTotalNumber++;
        }
    }
}
