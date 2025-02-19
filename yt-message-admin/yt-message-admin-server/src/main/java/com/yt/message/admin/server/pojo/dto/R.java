package com.yt.message.admin.server.pojo.dto;

import com.yt.message.common.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName R
 * @Description 统一返回
 * @Author Ts
 * @Version 1.0
 */
@Getter
@Setter
public class R <T> {

    private int code;
    private String message;
    private T data;

    private R(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> R<T> success(String message,T data) {
        return new R(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }
    public static <T> R<T> success(T data) {
        return success("请求成功",data);
    }

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> fail(String message) {
        return fail(ResultCodeEnum.FAIL.getCode(), message);
    }

    public static <T> R<T> fail() {
        return fail("请求失败");
    }



}
