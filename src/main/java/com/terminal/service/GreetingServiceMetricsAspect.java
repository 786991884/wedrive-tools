package com.terminal.service;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * @author Lenovo
 * @date 2016-12-05
 * @modify
 * @copyright
 */
@Aspect
@Component
public class GreetingServiceMetricsAspect {

    private final CounterService counterService;

    @Autowired
    public GreetingServiceMetricsAspect(CounterService counterService) {
        this.counterService = counterService;
    }

    @AfterReturning(pointcut = "execution(* com.terminal.service.GreetingService.getGreeting(int)) && args(number)", argNames = "number")
    public void afterCallingGetGreeting(int number) {
        counterService.increment("counter.calls.get_greeting");
        counterService.increment("counter.calls.get_greeting." + number);
    }

    @AfterThrowing(pointcut = "execution(* com.terminal.service.GreetingService.getGreeting(int))", throwing = "e")
    public void afterGetGreetingThrowsException(NoSuchElementException e) {
        counterService.increment("counter.errors.get_greeting");
    }

}