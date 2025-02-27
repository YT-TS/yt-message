package com.yt.message.handler.util;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.yt.message.admin.client.pojo.dto.PlatformToken;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName PlatformTokenUtil
 * @Description TODO
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class PlatformTokenUtil {

    private static LoadingCache<Long, PlatformToken> platformTokenLocalCache;

    public static void setPlatformLocalCache(LoadingCache<Long, PlatformToken> platformLocalCache) {
        PlatformTokenUtil.platformTokenLocalCache = platformLocalCache;
    }

    public static PlatformToken getPlatformToken(Long platformId) {
       return platformTokenLocalCache.get(platformId);
    }
    public static void removePlatformToken(Long platformId) {
        platformTokenLocalCache.invalidate(platformId);
    }

}
