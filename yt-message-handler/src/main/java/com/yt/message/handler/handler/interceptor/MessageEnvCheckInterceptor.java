package com.yt.message.handler.handler.interceptor;


import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageEnvCheckInterceptor
 * @Description 检查消息发送需要的环境
 * @Author Ts
 * @Version 1.0
 */
@Component
public class MessageEnvCheckInterceptor implements MessageHandlerInterceptor {

    @Override
    public HandleResult preHandle(MessageExt message, MessageSendPayload messageSendPayload) {
        //消息发送前需要的资源获取
        //模板本地缓存
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        if (templateCache == null) {
            return HandleResult.fail("模板不存在");
        }

        //平台本地缓存
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());
        if (platformCache == null) {
            return HandleResult.fail("平台不存在");
        }
        messageSendPayload.setPlatformId(platformCache.getPlatformId());
        return HandleResult.success();
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
