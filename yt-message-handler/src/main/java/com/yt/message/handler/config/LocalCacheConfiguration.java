package com.yt.message.handler.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.admin.client.service.CacheSyncService;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.yt.message.handler.constant.LocalCacheConstant.*;

/**
 * @ClassName LocalCacheConfiguration
 * @Author Ts
 * @Version 1.0
 */
@Configuration
@Slf4j
public class LocalCacheConfiguration  {



    @DubboReference(timeout = 5000, retries = 0, lazy = true)
    private CacheSyncService cacheSyncService;


    @Bean
    public LoadingCache<Long, PlatformCache> platformLocalCache() {
        LoadingCache<Long, PlatformCache> cache = Caffeine.newBuilder()
                .initialCapacity(PLATFORM_CACHE_INITIAL_CAPACITY)
                .maximumSize(PLATFORM_CACHE_MAXIMUM_CAPACITY)
                .expireAfterAccess(PLATFORM_CACHE_EXPIRE_TIME)
                .build(key -> {
                    //数据库找
                    return cacheSyncService.syncPlatform(key);
                });
        MessageLocalCacheUtil.setPlatformLocalCache(cache);
        return cache;
    }

    @Bean
    public LoadingCache<Long, TemplateCache> templateLocalCache() {
        LoadingCache<Long, TemplateCache> cache = Caffeine.newBuilder()
                .initialCapacity(TEMPLATE_CACHE_INITIAL_CAPACITY)
                .maximumSize(TEMPLATE_CACHE_MAXIMUM_CAPACITY)
                .expireAfterAccess(TEMPLATE_CACHE_EXPIRE_TIME)
                .build(key -> {
                    return cacheSyncService.syncTemplate(key);
                });
        MessageLocalCacheUtil.setTemplateLocalCache(cache);
        return cache;
    }





}
