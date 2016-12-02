package com.terminal.communication;

import com.terminal.util.Packet;

public abstract class TaCommand {
    /**
     * 接收消息处理类
     *
     * @param packet
     */
    public abstract void processor(Packet packet);
}
