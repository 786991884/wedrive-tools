package com.terminal.mbean;

import com.terminal.netty.NettyClientServer;
import io.netty.handler.traffic.TrafficCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class IoAcceptorStat implements IoAcceptorStatMBean {

    @Autowired
    private NettyClientServer nettyClientServer;

    private TrafficCounter getTrafficCounter() {
        return nettyClientServer.getTrafficCounter();
    }

    @Override
    public long lastReadThroughput() {
        return getTrafficCounter().lastReadThroughput();
    }

    @Override
    public long lastWriteThroughput() {
        return getTrafficCounter().lastWriteThroughput();
    }

    @Override
    public long lastReadBytes() {
        return getTrafficCounter().lastReadBytes();
    }

    @Override
    public long lastWrittenBytes() {
        return getTrafficCounter().lastWrittenBytes();
    }

    @Override
    public long currentReadBytes() {
        return getTrafficCounter().currentReadBytes();
    }

    @Override
    public long currentWrittenBytes() {
        return getTrafficCounter().currentWrittenBytes();
    }

    @Override
    public long lastTime() {
        return getTrafficCounter().lastTime();
    }

    @Override
    public long cumulativeWrittenBytes() {
        return getTrafficCounter().cumulativeWrittenBytes();
    }

    @Override
    public long cumulativeReadBytes() {
        return getTrafficCounter().cumulativeReadBytes();
    }

    @Override
    public long lastCumulativeTime() {
        return getTrafficCounter().lastCumulativeTime();
    }

    @Override
    public AtomicLong getRealWrittenBytes() {
        return getTrafficCounter().getRealWrittenBytes();
    }

    @Override
    public long getRealWriteThroughput() {
        return getTrafficCounter().getRealWriteThroughput();
    }

    @Override
    public String toString() {
        return getTrafficCounter().toString();
    }
}
