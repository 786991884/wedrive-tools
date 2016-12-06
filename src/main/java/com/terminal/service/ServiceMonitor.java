package com.terminal.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

/**
 * gague和counter度量通过GagueService和CountService实例提供，这些实例可以导入到任何Spring管理的对象中，用于度量应用信息。
 * 例如，我们可以统计某个方法的调用次数，如果要统计所有RESTful接口的调用次数，则可以通过AOP实现，在调用指定的接口之前，
 * 首先调用counterService.increment("objectName.methodName.invoked");，某个方法被调用之后，则对它的统计值+1
 * <p>
 * 表示在每个Controller的方法调用之前，首先增加调用次数
 * <p>
 * AOP方式监控方法的调用次数,可在admin ui上显示,主要依赖 actuate.metrics.CounterService
 *
 * @author Lenovo
 * @date 2016-12-05
 * @modify
 * @copyright
 */
@Aspect
@Component
public class ServiceMonitor {

    @Autowired
    private CounterService counterService;

    @Before("execution(* com.terminal.web.*.*(..))")
    public void countServiceInvoke(JoinPoint joinPoint) {
        counterService.increment(joinPoint.getSignature() + "");
    }

}
