package com.terminal.mbean;

import java.util.concurrent.atomic.AtomicLong;

public interface IoAcceptorStatMBean {


    /**
     * netty 读流量 bytes/s 对浏览器来说其实就是上传速度
     *
     * @return the Read Throughput in bytes/s computes in the last check interval.
     */
    long lastReadThroughput();

    /**
     * netty写流量 bytes/s 对浏览器来说其实就是下载速度
     *
     * @return the Write Throughput in bytes/s computes in the last check interval.
     */
    long lastWriteThroughput();

    /**
     * @return the number of bytes read during the last check Interval.
     */
    long lastReadBytes();

    /**
     * @return the number of bytes written during the last check Interval.
     */
    long lastWrittenBytes();

    /**
     * @return the current number of bytes read since the last checkInterval.
     */
    long currentReadBytes();

    /**
     * @return the current number of bytes written since the last check Interval.
     */
    long currentWrittenBytes();

    /**
     * @return the Time in millisecond of the last check as of System.currentTimeMillis().
     */
    long lastTime();

    /**
     * @return the cumulativeWrittenBytes
     */
    long cumulativeWrittenBytes();

    /**
     * @return the cumulativeReadBytes
     */
    long cumulativeReadBytes();

    /**
     * @return the lastCumulativeTime in millisecond as of System.currentTimeMillis()
     * when the cumulative counters were reset to 0.
     */
    long lastCumulativeTime();

    /**
     * @return the realWrittenBytes
     */
    AtomicLong getRealWrittenBytes();

    /**
     * @return the realWriteThroughput
     */
    long getRealWriteThroughput();
}
