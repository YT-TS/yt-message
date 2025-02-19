package com.yt.message.api.server.interceptor.chain;


import com.yt.message.api.server.interceptor.MessageSendInterceptor;
import com.yt.message.common.enums.MessageType;
import yt.message.api.client.pojo.dto.SendMessageRequest;

import java.util.List;

/**
 * @ClassName MessageSendInterceptorChain
 * @Description 拦截器链
 * @Author Ts
 * @Version 1.0
 */

public class MessageSendInterceptorChain {

    private List<MessageSendInterceptor> interceptors;

    public MessageSendInterceptorChain(List<MessageSendInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public void intercept(SendMessageRequest messageRequest, MessageType type ) {
        for (MessageSendInterceptor interceptor : interceptors) {
            if (interceptor.supports(type)) {
                interceptor.intercept(messageRequest);
            }
        }
    }
}
