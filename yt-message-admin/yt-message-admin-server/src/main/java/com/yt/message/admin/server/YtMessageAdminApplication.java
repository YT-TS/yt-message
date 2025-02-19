package com.yt.message.admin.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.yt.message.admin.server.mapper")
public class YtMessageAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtMessageAdminApplication.class, args);
    }

}

// netty
// 其他类型消息
// 消息附件
// 使用rocketmq流计算
// 安卓通知栏