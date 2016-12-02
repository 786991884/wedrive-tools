package com.simulation.terminal.communication.protocol;

import java.util.Date;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.Command;
import com.simulation.terminal.communication.MessageSend;
import com.simulation.terminal.entity.DriverCardInfo;
import com.simulation.terminal.util.Packet;

public class ReportDriverInfo extends Command {

	@Override
	public void processor(Packet packet) {
		packet.setMessageId(0x0702);
		packet.setSerialNumber(CacheManager.getSerialNumber());
		DriverCardInfo info = new DriverCardInfo();
		long current = new Date().getTime();
		info.setCardDate(current);
		info.setCertificateCode("驾驶员编码007");
		info.setDriverName("驾驶员姓名XX");
		info.setLicenseValidDate(current+2592000000l);
		info.setOrganizationName("中国交通部");
		info.setResult((byte)0x00);
		info.setStatus((byte)0x01);
		packet.setMessageBody(info.formatCardInfo());
		MessageSend send = new MessageSend();
		send.builder(packet);
	}
}
