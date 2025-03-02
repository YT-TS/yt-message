package com.yt.message.admin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.PlatformToken;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.admin.client.service.CacheSyncService;
import com.yt.message.admin.server.constant.CacheKey;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CacheSyncServiceImpl
 * @Author Ts
 * @Version 1.0
 */
@Service
//@DubboService(timeout = 3000)
@DubboService
@Slf4j
public class CacheSyncServiceImpl implements CacheSyncService {


    @Autowired
    private IPlatformService platformService;
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private PlatformTokenService platformTokenService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PlatformCache syncPlatform(Long platformId) {
        Platform platform = platformService.getById(platformId);
        log.info("同步信息{}", platformId);
        if (platform == null) {
            return null;
        }
        return BeanUtil.toBean(platform, PlatformCache.class);
    }

    @Override
    public TemplateCache syncTemplate(Long templateId) {
        Template template = templateService.getById(templateId);
        if (template == null) {
            return null;
        }
        return BeanUtil.toBean(template, TemplateCache.class);

    }

    @Override
    public PlatformToken syncPlatformToken(Long platformId) {
        log.info("同步token");
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        Map<String, Object> valueMap = hashOperations.entries(CacheKey.PLATFORM_TOKEN.getKey(platformId));
        if (CollUtil.isNotEmpty(valueMap)) {
            //缓存存在
            Object expireTimestamp = valueMap.get("expireTimestamp");
            Long expireTimestampLong = expireTimestamp instanceof Long ? (Long) expireTimestamp : Long.valueOf(expireTimestamp.toString());
            Object lifespan = valueMap.get("lifespan");
            Long lifespanLong = lifespan instanceof Long ? (Long) lifespan : Long.valueOf(lifespan.toString());
            return new PlatformToken(platformId,valueMap.get("token").toString(),expireTimestampLong,lifespanLong);
        }
        //缓存不存在
        PlatformToken platformToken = platformTokenService.getPlatformToken(platformId);
        if (platformToken != null) {
            //设置缓存
            HashMap<String, Object> hash = new HashMap<>();
            hash.put("platformId", platformId);
            hash.put("token", platformToken.getAccessToken());
            hash.put("expireTimestamp",platformToken.getExpireTimestamp());
            hash.put("lifespan", platformToken.getLifespan());
            hashOperations.putAll(CacheKey.PLATFORM_TOKEN.getKey(platformId),hash );
            redisTemplate.expire(CacheKey.PLATFORM_TOKEN.getKey(platformId), Duration.ofSeconds(platformToken.getLifespan()));
            return platformToken;
        }
        return null;
    }


}
