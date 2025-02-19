package com.yt.message.handler.handler.interceptor;

/**
 * @ClassName HandleResult
 * @Description 前置处理结果
 * @Author Ts
 * @Version 1.0
 */

public class HandleResult {

    private  boolean result;
    private  String msg;

    public HandleResult(boolean result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public boolean getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public static HandleResult success() {
        return new HandleResult(true, "");
    }
    public static HandleResult fail(String msg) {
        return new HandleResult(false, msg);
    }

}
