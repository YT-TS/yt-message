package com.yt.message.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ResultCodeEnums
 * @Author Ts

 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {
    SUCCESS(0),
    FAIL(-1);
    private int code;
}
