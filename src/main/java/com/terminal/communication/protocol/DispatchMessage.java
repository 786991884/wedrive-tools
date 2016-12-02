package com.terminal.communication.protocol;

import com.terminal.communication.LocationCommand;
import com.terminal.communication.TaCommand;
import com.terminal.util.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

@LocationCommand(id = 0x8300)
public class DispatchMessage extends TaCommand {
    public static Logger logger = LoggerFactory.getLogger(DispatchMessage.class);

    @Override
    public void processor(Packet packet) {
        byte[] body = packet.getMessageBody();
        if ((body[0] & 0x01) > 0) {
            logger.info("调度短信：紧急");
        } else if ((body[0] & 0x04) > 0) {
            logger.info("调度短信：终端显示器显示");
        } else if ((body[0] & 0x08) > 0) {
            logger.info("调度短信：终端TTS 播读");
        } else if ((body[0] & 0x10) > 0) {
            logger.info("调度短信：广告屏显示");
        } else if ((body[0] & 0x10) > 0) {
            logger.info("CAN 故障码信息");
        }
        byte[] content = new byte[body.length - 1];
        System.arraycopy(body, 1, content, 0, content.length);
        try {
            logger.info(new String(content, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
