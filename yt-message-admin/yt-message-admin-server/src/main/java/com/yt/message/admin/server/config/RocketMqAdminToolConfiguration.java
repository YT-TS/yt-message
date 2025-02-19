package com.yt.message.admin.server.config;

import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RocketMqAdminToolConfiguration
 * @Description 配置rocketmq命令行工具
 * @Author Ts
 * @Version 1.0
 */
@Configuration
public class RocketMqAdminToolConfiguration {
//    private final RocketMQProperties rocketMQProperties;
//
//    public RocketMqAdminToolConfiguration(RocketMQProperties rocketMQProperties) {
//        this.rocketMQProperties = rocketMQProperties;
//    }
//
//
//    @Bean
//    public DefaultMQAdminExt mqAdminExt() throws MQClientException {
//
//        //nameserver地址
//        String nameServer = rocketMQProperties.getNameServer();
//        if (StrUtil.isNotBlank(nameServer)){
//            System.setProperty(MixAll.NAMESRV_ADDR_PROPERTY, nameServer);
//        }else {
//            System.setProperty(MixAll.NAMESRV_ADDR_PROPERTY, "127.0.0.1:9876");
//        }
//
//        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
//        defaultMQAdminExt.setVipChannelEnabled(true);
//        defaultMQAdminExt.start();
//        return defaultMQAdminExt;
//    }
//
//    @Bean
//    public DefaultLitePullConsumer adminToolConsumer () {
//        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer(MixAll.TOOLS_CONSUMER_GROUP);
//        consumer.setUseTLS(false);
//        return consumer;
//    }

}
