package com.yt.message.api.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class YtMessageApiProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtMessageApiProviderApplication.class, args);
    }

}
