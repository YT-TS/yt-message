package com.yt.message.admin.server.constant;

import com.yt.message.common.constant.ICacheKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @ClassName CacheKey
 * @Author Ts
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
public enum CacheKey  implements ICacheKey {
    PLATFORM_TOKEN("YT-MESSAGE:ADMIN:PLATFORM_TOKEN",null),

    ;
    private String prefix;

    private Duration expire;


}
