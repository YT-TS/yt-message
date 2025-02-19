package com.yt.message.handler.config;

import com.yt.message.handler.handler.chain.MessageHandlerChain;
import com.yt.message.handler.handler.interceptor.MessageHandlerInterceptor;
import com.yt.message.handler.handler.send.DelegatingMessageHandler;
import com.yt.message.handler.handler.send.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @ClassName ConsumerInfrastructureConfiguration
 * @Author Ts
 * @Version 1.0
 */
@Configuration
public class ConsumerInfrastructureConfiguration {

    private final List<MessageHandler> messageHandlers;

    public ConsumerInfrastructureConfiguration( List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    //生成消息处理器
    @Bean
    public MessageHandler delegatingMessageHandler() {
        return new DelegatingMessageHandler(messageHandlers);
    }

    //配置消息处理链
    @Bean
    public MessageHandlerChain messageHandlerChain(List<MessageHandlerInterceptor> interceptors) {
        MessageHandlerChain messageHandlerChain = new MessageHandlerChain(delegatingMessageHandler());
        messageHandlerChain.addMessageSendInterceptors(interceptors);
        return messageHandlerChain;
    }

}
