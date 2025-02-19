package com.yt.message.handler.handler.interceptor;

import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.enums.ResultCodeEnum;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.constant.ConsumerCacheKey;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageRepeatConsumeInterceptor
 * @Description 重复消费校验
 * @Author Ts
 * @Version 1.0
 */

@Component
@Slf4j
public class MessageRepeatConsumeInterceptor implements MessageHandlerInterceptor {

    private final RedisTemplate redisTemplate;

    public MessageRepeatConsumeInterceptor(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public HandleResult preHandle(MessageExt message, MessageSendPayload messageSendPayload) {
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());


        Integer repeatConsume = templateCache.getRequireRepeatConsume();
        if (YesOrNoEnum.YES.getValue().equals(repeatConsume)) {
            String messageId = message.getProperty(messageSendPayload.getMessageId().toString());
//            if (!StrUtil.isNotEmpty(messageId)) {
//                //缺少参数
//                return false;
//            }
            Boolean setResult = redisTemplate.opsForValue().setIfAbsent(ConsumerCacheKey.REPEAT_CONSUME.getKey(messageId), messageId, ConsumerCacheKey.REPEAT_CONSUME.getExpire());
            if (setResult != null && !setResult) {
                //重复消费
                log.info(messageId + "重复消费");
                return HandleResult.fail("重复消费");
            }
        }
        return HandleResult.success();
    }

    @Override
    public void finalHandle(MessageExt message, MessageSendPayload messageSendPayload, MessageSendResult result, Exception ex){
        //没有发送或发送失败才清除
        //如果是没有发送（result == null）肯定是出现了异常
        // result == null ex肯定不为null
        if (result == null || result.getCode() != ResultCodeEnum.SUCCESS.getCode()) {
            //发送失败要清除key
            TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
            Integer repeatConsume = templateCache.getRequireRepeatConsume();
            if (YesOrNoEnum.YES.getValue().equals(repeatConsume)) {
                String messageId = messageSendPayload.getMessageId().toString();
                redisTemplate.delete(ConsumerCacheKey.REPEAT_CONSUME.getKey(messageId));
            }
        }
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE - 1;
    }
}
