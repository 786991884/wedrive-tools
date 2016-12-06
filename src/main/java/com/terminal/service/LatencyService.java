package com.terminal.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;

/**
 * AOP方式监控方法的调用时长,可在admin ui上显示,主要依赖 actuate.metrics.GaugeService
 *
 * @author Lenovo
 * @date 2016-12-05
 * @modify
 * @copyright
 */
public class LatencyService {

    @Autowired
    private GaugeService gaugeService;

    /**
     * 如果希望统计每个接口的调用时长，则需要借助GagueService来实现，同样使用AOP实现，
     * 则需要环绕通知：在接口调用之前，利用long start = System.currentTimeMillis();，
     * 在接口调用之后，计算耗费的时间，单位是ms，然后使用gugeService.submit(latency)更新该接口的调用延时。
     *
     * @param pjp
     * @throws Throwable
     */
    @Around("execution(* com.terminal.web.*.*(..))")
    public void latencyService(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        pjp.proceed();
        long end = System.currentTimeMillis();
        gaugeService.submit(pjp.getSignature().toString(), end - start);
    }


}