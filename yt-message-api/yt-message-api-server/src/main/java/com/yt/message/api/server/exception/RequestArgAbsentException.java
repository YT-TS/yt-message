package com.yt.message.api.server.exception;

/**
 * @ClassName RequestArgAbsentException
 * @Description 发送请求参数缺失异常
 * @Author Ts
 * @Version 1.0
 */

public class RequestArgAbsentException extends InterceptorException {
    static final long serialVersionUID = -444888888L;

    public RequestArgAbsentException(String message) {
        super(message);
    }



}
