package com.yt.message.api.server.interceptor;

import com.yt.message.api.server.exception.InterceptorException;
import com.yt.message.common.enums.MessageType;
import yt.message.api.client.pojo.dto.SendMessageRequest;

/**
 * @ClassName MessageSendInterceptor
 * @Description 消息发送拦截器
 * @Author Ts
 * @Version 1.0
 */

public interface MessageSendInterceptor {


    void intercept(SendMessageRequest messageRequest) throws InterceptorException;

    /**
     * 是否支持拦截消息
     * @param messageType 消息类型
     * @return boolean
     */
    boolean supports (MessageType messageType);
}
