package com.terminal.communication.protocol;


import com.terminal.cache.CacheManager;
import com.terminal.communication.LocationCommand;
import com.terminal.communication.MessageSend;
import com.terminal.communication.TaCommand;
import com.terminal.entity.DriverCardInfo;
import com.terminal.util.Packet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@LocationCommand(id = 0x8702)
public class ReportDriverInfo extends TaCommand {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void processor(Packet packet) {
        packet.setMessageId(0x0702);
        packet.setSerialNumber(cacheManager.getSerialNumber());
        DriverCardInfo info = new DriverCardInfo();
        long current = new Date().getTime();
        info.setCardDate(current);
        info.setCertificateCode("驾驶员编码007");
        info.setDriverName("驾驶员姓名XX");
        info.setLicenseValidDate(current + 2592000000l);
        info.setOrganizationName("中国交通部");
        info.setResult((byte) 0x00);
        info.setStatus((byte) 0x01);
        packet.setMessageBody(info.formatCardInfo());
        MessageSend send = new MessageSend();
        send.builder(packet);
    }
}
