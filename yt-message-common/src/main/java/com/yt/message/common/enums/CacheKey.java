package com.yt.message.common.enums;

import com.yt.message.common.constant.ICacheKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @ClassName CacheKey
 * @Author Ts
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum CacheKey implements ICacheKey {
    YT_STATISTICS_BIZ_AMOUNT("YT:STATISTICS_BIZ_AMOUNT", null);

    private final String prefix;
    private final Duration expire;


}
