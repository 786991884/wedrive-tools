package com.terminal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.repository.MetricRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 导出监控信息
 *
 * @author Lenovo
 * @date 2016-12-05
 * @modify
 * @copyright
 */
@Service
public class MetricExporterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricExporterService.class);
    private final MetricRepository repository;

    @Autowired
    public MetricExporterService(MetricRepository repository) {
        this.repository = repository;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void exportMetrics() {
        repository.findAll().forEach(this::log);
    }

    private void log(Metric<?> m) {
        //LOGGER.info(append("metric", m), "Reporting metric {}={}", m.getName(), m.getValue());
        LOGGER.info("Reporting metric {}={}", m.getName(), m.getValue());
        repository.reset(m.getName());
    }
}
