package com.terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * @author Lenovo
 * @date 2016-12-05
 * @modify
 * @copyright
 */
@Service
public class GreetingServiceImpl implements GreetingService {

    private static final String[] GREETINGS = {"Yo!", "Hello", "Good day", "Hi", "Hey"};

    private final CounterService counterService;

    @Autowired
    public GreetingServiceImpl(CounterService counterService) {
        this.counterService = counterService;
    }

    @Override
    public String getGreeting(int number) {
        if (number < 1 || number > GREETINGS.length) {
            counterService.increment("counter.errors.get_greeting");
            throw new NoSuchElementException(String.format("No greeting #%d", number));
        }
        counterService.increment("counter.calls.get_greeting");
        counterService.increment("counter.calls.get_greeting." + (number - 1));
        return GREETINGS[number - 1];
    }

}