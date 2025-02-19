package com.yt.message.api.server.exception;

/**
 * @ClassName InterceptorException
 * @Description 拦截器抛出的异常 会中断消息发送
 * @Author Ts
 * @Version 1.0
 */

public class InterceptorException extends RuntimeException {

    public InterceptorException(String message) {
        super(message);
    }
}
