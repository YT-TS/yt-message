package com.yt.message.handler.handler.interceptor;


import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.enums.RateLimitStrategy;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @ClassName MessageRateLimitInterceptor
 * @Author Ts
 * @Description 限流
 * @Version 1.0
 */
@Slf4j
@Component
public class MessageRateLimitInterceptor implements MessageHandlerInterceptor {
    private final RedissonClient redissonClient;

    public MessageRateLimitInterceptor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public HandleResult preHandle(MessageExt message, MessageSendPayload messageSendPayload) {

        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        if (YesOrNoEnum.YES.getValue().equals(templateCache.getRequireRateLimit())) {
            //需要限流
            String rateLimitKey = platformCache.getRateLimitKey();
            //限流规模
            Integer rateLimitScale = platformCache.getRateLimitScale();
            //限流时间间隔
            Integer rateLimitInterval = platformCache.getRateLimitInterval();
            if (rateLimitKey == null || rateLimitScale == null || rateLimitInterval == null){
                return HandleResult.fail("限流参数缺失");
            }
//
            //限流
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimitKey);
            if (!rateLimiter.isExists()) {
                rateLimiter.trySetRate(RateType.OVERALL, rateLimitScale, Duration.ofSeconds(rateLimitInterval), Duration.ofSeconds(rateLimitInterval + 5));
            }
            if (RateLimitStrategy.WAIT.getCode().equals(templateCache.getRateLimitStrategy())){
                //等待
                rateLimiter.acquire();
            }else {
                if (!rateLimiter.tryAcquire()){
                    //未获得许可
                    return HandleResult.fail("限流拒绝发送");
                }
            }

        }

        return HandleResult.success();
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
