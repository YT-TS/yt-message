package com.yt.message.admin.server.exception;

/**
 * @ClassName BusinessException
 * @Description 业务异常
 * @Author Ts
 * @Version 1.0
 */

public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 2L;

    public BusinessException(String message) {
        super(message);
    }
}