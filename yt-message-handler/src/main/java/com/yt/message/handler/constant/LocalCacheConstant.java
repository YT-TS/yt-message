package com.yt.message.handler.constant;

import java.time.Duration;

/**
 * @ClassName LocalCacheConstant
 * @Author Ts
 * @Version 1.0
 */

public class LocalCacheConstant {


    public static final int PLATFORM_CACHE_INITIAL_CAPACITY  = 10;
    public static final int PLATFORM_CACHE_MAXIMUM_CAPACITY  = 30;
    public static final Duration PLATFORM_CACHE_EXPIRE_TIME = Duration.ofDays(3);

    public static final int TEMPLATE_CACHE_INITIAL_CAPACITY  = 20;
    public static final int TEMPLATE_CACHE_MAXIMUM_CAPACITY  = 100;
    public static final Duration TEMPLATE_CACHE_EXPIRE_TIME = Duration.ofDays(5);



}
