package com.yt.message.handler.handler.send;


import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName DelegatingMessageHandler
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class DelegatingMessageHandler implements MessageHandler {
    private Map<String, MessageHandler> handlers;


    public DelegatingMessageHandler(List<MessageHandler> messageHandlers) {
        handlers = messageHandlers.stream().collect(Collectors.toMap(MessageHandler::getName, item -> item));
        for (MessageHandler messageHandler : messageHandlers) {
            log.info("初始化消息处理器:{}", messageHandler.getName());
        }
    }
    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload){
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());

        MessageHandler handler = handlers.get(platformCache.getHandlerName());
        if (handler == null){
            return MessageSendResult.fail("未找到对应的消息处理器");
        }
        return  handler.handle(message, messageSendPayload);
    }

    @Override
    public String getName() {
        return "delegateHandler";
    }
}
