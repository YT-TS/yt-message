package com.yt.message.admin.server.exception;

/**
 * @ClassName IllegalRequestException
 * @Description 非法请求异常 一般指绕过前端发起的请求
 * @Author Ts
 * @Version 1.0
 */

public class IllegalRequestException  extends RuntimeException{
    private static final long serialVersionUID = 3L;

    public IllegalRequestException(String message) {
        super(message);
    }
}
