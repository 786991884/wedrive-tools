package com.terminal.cache;

import com.google.common.cache.*;
import com.terminal.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Lenovo
 * @date 2016-11-21
 * @modify
 * @copyright
 */
@Component
public class CommandCache {
    private static final Logger logger = LoggerFactory.getLogger(CommandCache.class);

    public static AtomicLong totalMapSize_5 = new AtomicLong(0);
    public static AtomicLong totalMapSize_10 = new AtomicLong(0);
    public static AtomicLong totalMapSize_15 = new AtomicLong(0);
    public static AtomicLong totalMapSize_30 = new AtomicLong(0);
    public static AtomicLong expireMapSize_5 = new AtomicLong(0);
    public static AtomicLong expireMapSize_10 = new AtomicLong(0);
    public static AtomicLong expireMapSize_15 = new AtomicLong(0);
    public static AtomicLong expireMapSize_30 = new AtomicLong(0);
    public static AtomicLong explicitMapSize_5 = new AtomicLong(0);
    public static AtomicLong explicitMapSize_10 = new AtomicLong(0);
    public static AtomicLong explicitMapSize_15 = new AtomicLong(0);
    public static AtomicLong explicitMapSize_30 = new AtomicLong(0);

    //public Map<String, String> totalMap = new ConcurrentHashMap<>();

    //public Map<String, String> expireMap = new ConcurrentHashMap<>();

    //public Map<String, String> explicitMap = new ConcurrentHashMap<>();

    @Autowired
    private ConfigUtils configUtils;

    /*周期性线程池*/
    private ScheduledExecutorService executorService_5 = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService executorService_10 = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService executorService_15 = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService executorService_30 = Executors.newScheduledThreadPool(1);
    //key:终端id，value:消息流水号
    private Cache<String, Long> cache_5 = null;
    private Cache<String, Long> cache_10 = null;
    private Cache<String, Long> cache_15 = null;
    private Cache<String, Long> cache_30 = null;

    /*private Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireTime, TimeUnit.SECONDS)//设置写缓存后3秒钟过期
            .maximumSize(10000).removalListener(RemovalListeners.asynchronous(new RemovalListener<Object, String>() {
                @Override
                public void onRemoval(RemovalNotification<Object, String> notification) {
                    logger.info("key=" + notification.getKey() + "value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                    //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                    if (notification.getCause().name().equals("EXPIRED")) {
                        DownStatusCommand downCommand = new DownStatusCommand();
                        if (notification.getValue() != null) {
                            downCommand.setId(notification.getValue());
                        }
                        downCommand.setState(DownCommandState.W_TIMEOUT);
                        messageChannel.send(downCommand);
                    }
                }
            }, executorService))
            *//*removalListener(new RemovalListener<Object, CommandModel>() {
                @Override
                public void onRemoval(RemovalNotification<Object, CommandModel> notification) {
                    System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                }
            })*//*
            .build();*/
    public void add_5(String id, Long deviceCommand) {
        cache_5.put(id, deviceCommand);
        //被观察者
    }

    public void add_10(String id, Long deviceCommand) {
        cache_10.put(id, deviceCommand);
        //被观察者
    }

    public void add_15(String id, Long deviceCommand) {
        cache_15.put(id, deviceCommand);
    }

    public void add_30(String id, Long deviceCommand) {
        cache_30.put(id, deviceCommand);
    }

    public Long get_5(String id) {
        return cache_5.getIfPresent(id);
    }

    public Long get_10(String id) {
        return cache_10.getIfPresent(id);
    }

    public Long get_15(String id) {
        return cache_15.getIfPresent(id);
    }

    public Long get_30(String id) {
        return cache_30.getIfPresent(id);
    }

    public void remove_5(String id) {
        cache_5.invalidate(id);
    }

    public void remove_10(String id) {
        cache_10.invalidate(id);
    }

    public void remove_15(String id) {
        cache_15.invalidate(id);
    }

    public void remove_30(String id) {
        cache_30.invalidate(id);
    }

    public long getSize() {
        return cache_5.size();
    }

    @PostConstruct
    protected void executeCleanCache() {
        cache_5 = CacheBuilder.newBuilder()
                .expireAfterWrite(configUtils.getExpireTime_5(), TimeUnit.MILLISECONDS)//设置写缓存后3秒钟过期
                .maximumSize(Integer.MAX_VALUE).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, Long>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Long> notification) {
                        //logger.info("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        //totalMap.put(notification.getKey().toString(), notification.getValue());
                        totalMapSize_5.getAndIncrement();
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                        if (notification.getCause().name().equals("EXPIRED")) {//过期移除
                            //expireMap.put(notification.getKey().toString(), notification.getValue());
                            expireMapSize_5.getAndIncrement();
                        } else if (notification.getCause().name().equals("EXPLICIT")) {  //显式移除
                            //explicitMap.put(notification.getKey().toString(), notification.getValue());
                            explicitMapSize_5.getAndIncrement();
                        } else {
                            logger.error("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        }
                    }
                }, executorService_5)).build();
        cache_10 = CacheBuilder.newBuilder()
                .expireAfterWrite(configUtils.getExpireTime_10(), TimeUnit.MILLISECONDS)//设置写缓存后3秒钟过期
                .maximumSize(Integer.MAX_VALUE).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, Long>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Long> notification) {
                        //logger.info("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        //totalMap.put(notification.getKey().toString(), notification.getValue());
                        totalMapSize_10.getAndIncrement();
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                        if (notification.getCause().name().equals("EXPIRED")) {//过期移除
                            //expireMap.put(notification.getKey().toString(), notification.getValue());
                            expireMapSize_10.getAndIncrement();
                        } else if (notification.getCause().name().equals("EXPLICIT")) {  //显式移除
                            //explicitMap.put(notification.getKey().toString(), notification.getValue());
                            explicitMapSize_10.getAndIncrement();
                        } else {
                            logger.error("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        }
                    }
                }, executorService_10)).build();
        cache_15 = CacheBuilder.newBuilder()
                .expireAfterWrite(configUtils.getExpireTime_15(), TimeUnit.MILLISECONDS)//设置写缓存后3秒钟过期
                .maximumSize(Integer.MAX_VALUE).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, Long>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Long> notification) {
                        //logger.info("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        //totalMap.put(notification.getKey().toString(), notification.getValue());
                        totalMapSize_15.getAndIncrement();
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                        if (notification.getCause().name().equals("EXPIRED")) {//过期移除
                            //expireMap.put(notification.getKey().toString(), notification.getValue());
                            expireMapSize_15.getAndIncrement();
                        } else if (notification.getCause().name().equals("EXPLICIT")) {  //显式移除
                            //explicitMap.put(notification.getKey().toString(), notification.getValue());
                            explicitMapSize_15.getAndIncrement();
                        } else {
                            logger.error("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());

                        }
                    }
                }, executorService_15)).build();
        cache_30 = CacheBuilder.newBuilder()
                .expireAfterWrite(configUtils.getExpireTime(), TimeUnit.MILLISECONDS)//设置写缓存后3秒钟过期
                .maximumSize(Integer.MAX_VALUE).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, Long>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Long> notification) {
                        //logger.info("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        //totalMap.put(notification.getKey().toString(), notification.getValue());
                        totalMapSize_30.getAndIncrement();
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                        if (notification.getCause().name().equals("EXPIRED")) {//过期移除
                            //expireMap.put(notification.getKey().toString(), notification.getValue());
                            expireMapSize_30.getAndIncrement();
                        } else if (notification.getCause().name().equals("EXPLICIT")) {  //显式移除
                            //explicitMap.put(notification.getKey().toString(), notification.getValue());
                            explicitMapSize_30.getAndIncrement();
                        } else {
                            logger.error("key=" + notification.getKey() + ",value=" + notification.getValue() + " was removed, cause is " + notification.getCause());

                        }
                    }
                }, executorService_30)).build();
        /*参数说明
         * 执行的线程任务
		 * 初始化延迟1秒则后执行
		 * 每隔1秒钟执行一次清除,本次执行结束后延迟1秒钟开始下次执行
		 * 时间单位(秒)
		 * */
        executorService_5.scheduleWithFixedDelay(new ClearUpExpired_5(), 1L, 1L, TimeUnit.MILLISECONDS);
        executorService_10.scheduleWithFixedDelay(new ClearUpExpired_10(), 1L, 1L, TimeUnit.MILLISECONDS);
        executorService_15.scheduleWithFixedDelay(new ClearUpExpired_15(), 1L, 1L, TimeUnit.MILLISECONDS);
        executorService_30.scheduleWithFixedDelay(new ClearUpExpired_30(), 1L, 1L, TimeUnit.MILLISECONDS);
    }

    /**
     * @Description: 任务执行完毕之后, 关闭调度线程池
     */
    protected void shutdown() {
        executorService_5.shutdown();
    }

    /**
     * @Description: 立即关闭所有正在执行的线程
     */
    protected void shutdownNow() {
        executorService_5.shutdownNow();
    }

    /**
     * 清除过期的缓存元素
     */
    private class ClearUpExpired_5 implements Runnable {
        @Override
        public void run() {
            cache_5.cleanUp();
        }
    }

    private class ClearUpExpired_10 implements Runnable {
        @Override
        public void run() {
            cache_10.cleanUp();
        }
    }

    private class ClearUpExpired_15 implements Runnable {
        @Override
        public void run() {
            cache_15.cleanUp();
        }
    }

    private class ClearUpExpired_30 implements Runnable {
        @Override
        public void run() {
            cache_30.cleanUp();
        }
    }

}