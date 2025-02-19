package com.yt.message.api.server.exception;

/**
 * @ClassName RequestArgFormatException
 * @Description 发送请求参数格式异常
 * @Author Ts
 * @Version 1.0
 */

public class RequestArgFormatException extends InterceptorException{
    static final long serialVersionUID = -444666888L;
    public RequestArgFormatException(String message) {
        super(message);
    }
}
