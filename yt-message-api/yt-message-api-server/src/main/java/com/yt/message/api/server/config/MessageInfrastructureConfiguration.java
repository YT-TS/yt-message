package com.yt.message.api.server.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.yt.message.api.server.interceptor.MessageSendInterceptor;
import com.yt.message.api.server.interceptor.chain.MessageSendInterceptorChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @ClassName MessageInfrastructureConfiguration
 * @Author Ts
 * @Version 1.0
 */
@Configuration
public class MessageInfrastructureConfiguration {


    @Bean
    public Snowflake messageIdUtils(){
        //TODO wordId 和 datacenterId 不能写死
        return IdUtil.getSnowflake(16,16);
    }

    @Bean
    public MessageSendInterceptorChain messageSendInterceptorChain(List<MessageSendInterceptor> interceptors){
        return new MessageSendInterceptorChain(interceptors);
    }
}
