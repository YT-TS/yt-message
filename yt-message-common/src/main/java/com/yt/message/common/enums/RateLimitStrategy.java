package com.yt.message.common.enums;

import lombok.Getter;

/**
 * @ClassName RateLimitStrategy
 * @Author Ts
 * @Version 1.0
 */
@Getter
public enum RateLimitStrategy {

    WAIT(1,"等待"),
    REJECT(2,"拒绝"),
    ;

    private final Integer code;
    private final String desc;

    RateLimitStrategy(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static RateLimitStrategy getByCode(Integer code) {
        for (RateLimitStrategy value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
