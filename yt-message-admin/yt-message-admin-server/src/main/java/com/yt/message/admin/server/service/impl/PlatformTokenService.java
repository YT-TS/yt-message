package com.yt.message.admin.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.yt.message.admin.client.pojo.dto.PlatformToken;
import com.yt.message.admin.server.config.WeChatApiUrl;
import com.yt.message.admin.server.constant.CacheKey;
import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.common.enums.DataSyncOpeType;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.pojo.dto.DataSyncMessagePayload;
import com.yt.message.mq.constant.MQConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

/**
 * @ClassName PlatformTokenService
 * @Description 平台token服务
 * @Author Ts
 * @Version 1.0
 */
@Service
@Slf4j
public class PlatformTokenService {

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private IPlatformService platformService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public List<Map<String, Object>> list() {
        Set<String> keys = redisTemplate.keys(CacheKey.PLATFORM_TOKEN.getKey("*"));
        List<Map<String, Object>> mapList = new ArrayList<>();
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        List<Object> objects = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                HashOperations<K, String, Object> opsForHash = operations.opsForHash();
                for (String key : keys) {
                    opsForHash.entries((K) key);
                }
                return null;
            }
        });
        for (Object object : objects) {
            Map<String, Object> map = (Map<String, Object>) object;
            map.put("platformId", map.get("platformId").toString());
            map.remove("token");
            mapList.add(map);
        }
        return mapList;
    }

    public PlatformToken getPlatformToken(Long platformId) {
        Platform platform = platformService.getById(platformId);
        if (platform == null) {
            log.error("平台不存在 平台id{}", platformId);
            return null;
        }
        return switch (MessageType.getByCode(platform.getMessageType())) {
            case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE -> weChatPublicPlatformAccessToken(platform);
            default -> null;
        };
    }

    public void update(String id) throws Exception {
        long platformId = Long.parseLong(id);
        PlatformToken platformToken = getPlatformToken(platformId);
        ExceptionAssert.throwOnFalse(platformToken != null, new BusinessException("平台token更新失败"));
        //设置缓存
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("platformId", platformId);
        hash.put("token", platformToken.getAccessToken());
        hash.put("expireTimestamp", platformToken.getExpireTimestamp());
        hash.put("lifespan", platformToken.getLifespan());
        redisTemplate.opsForHash().putAll(CacheKey.PLATFORM_TOKEN.getKey(platformId), hash);
        redisTemplate.expire(CacheKey.PLATFORM_TOKEN.getKey(platformId), Duration.ofSeconds(platformToken.getLifespan()));
        //通知删除本地缓存
        DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.PLATFORM_TOKEN_UPDATE, platformId, null);
        rocketMQTemplate.syncSend(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build());

    }

    private PlatformToken weChatPublicPlatformAccessToken(Platform platform) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("grant_type", "client_credential");
        queryMap.put("appid", platform.getAppId());
        queryMap.put("secret", platform.getSecretKey());

        String url = WeChatApiUrl.PUBLIC_PLATFORM_ACCESS_TOKEN + "?" + URLUtil.buildQuery(queryMap, StandardCharsets.UTF_8);
        HttpGet httpPost = new HttpGet(url);
        try {
            String result = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
            WeChatMiniProgramResponse response = JSONUtil.toBean(result, WeChatMiniProgramResponse.class);
            if (result != null) {
                if (response.getAccess_token() == null) {
                    log.error("微信公众号平台获取access_token失败,错误码={} 错误信息={}", response.getErrcode(), response.getErrmsg());
                    return null;
                }
                //过期时间提前五分钟，防止时间边界问题
                long msTimeStamp = DateUtil.offsetMinute(DateUtil.offsetSecond(new Date(), response.getExpires_in()), -5).getTime();
                return new PlatformToken(platform.getPlatformId(),response.getAccess_token(), msTimeStamp, response.getExpires_in().longValue() - 300);
            }
            return null;
        } catch (IOException e) {
            log.error("微信公众号平台获取access_token失败,http请求异常", e);
            return null;
        }
    }


    @Data
    static class WeChatMiniProgramResponse {

        private String access_token;
        /**
         * 有效时间单位s
         */
        private Integer expires_in;

        /**
         * Error code
         */
        private Integer errcode;

        /**
         * Error message
         */
        private String errmsg;
    }


}
