package com.terminal;

import com.terminal.entity.Message;
import com.terminal.service.InMemoryMessageRepository;
import com.terminal.service.MessageRepository;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
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

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

    @Bean
    public Converter<String, Message> messageConverter() {
        return new Converter<String, Message>() {
            @Override
            public Message convert(String id) {
                return messageRepository().findMessage(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        //app.setWebEnvironment(true);
        app.run(args);
    }

}
