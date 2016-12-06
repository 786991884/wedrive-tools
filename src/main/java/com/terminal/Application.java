package com.terminal;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Lenovo
 * @date 2016-11-16
 * @modify
 * @copyright
 */
@SpringBootApplication
@EnableAdminServer
@EnableScheduling
@EnableDiscoveryClient
public class Application {

    public static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        //app.setWebEnvironment(true);
        app.run(args);
    }

}
