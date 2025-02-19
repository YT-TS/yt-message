package com.yt.message.handler.util;


import com.github.benmanes.caffeine.cache.LoadingCache;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;

/**
 * @ClassName LocalCacheUtil
 * @Author Ts
 * @Version 1.0
 */
public class MessageLocalCacheUtil {

    private static LoadingCache<Long, PlatformCache> platformLocalCache;
    private static  LoadingCache<Long, TemplateCache> templateLocalCache;

    public static void setPlatformLocalCache(LoadingCache<Long, PlatformCache> platformLocalCache) {
        MessageLocalCacheUtil.platformLocalCache = platformLocalCache;
    }

    public static void setTemplateLocalCache(LoadingCache<Long, TemplateCache> templateLocalCache) {
        MessageLocalCacheUtil.templateLocalCache = templateLocalCache;
    }

    public static PlatformCache getPlatform(Long platformId) {
        return platformLocalCache.get(platformId);
    }
    public static void   removePlatform(Long platformId) {
        platformLocalCache.invalidate(platformId);
    }
    public static TemplateCache getTemplate(Long templateId) {
        return templateLocalCache.get(templateId);
    }

    public static void   removeTemplate(Long templateId) {
        templateLocalCache.invalidate(templateId);
    }
}