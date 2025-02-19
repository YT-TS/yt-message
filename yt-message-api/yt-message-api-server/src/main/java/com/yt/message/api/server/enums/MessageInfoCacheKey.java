package com.yt.message.api.server.enums;

import com.yt.message.common.constant.ICacheKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @ClassName CommonCacheKey
 * @Author Ts
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum MessageInfoCacheKey implements ICacheKey {


    TEMPLATE_INFO("TEMPLATE_INFO", Duration.ofDays(7)),
    PLATFORM_INFO("PLATFORM_INFO", null)


    ;
    private String prefix;

    private Duration expire;


}
