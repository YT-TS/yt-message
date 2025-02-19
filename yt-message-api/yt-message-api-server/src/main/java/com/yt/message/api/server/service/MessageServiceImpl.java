package com.yt.message.api.server.service;

import com.yt.message.api.server.interceptor.chain.MessageSendInterceptorChain;
import com.yt.message.api.server.util.MessageIdUtils;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import yt.message.api.client.pojo.dto.MessageSendRsp;
import yt.message.api.client.pojo.dto.SendMessageRequest;
import yt.message.api.client.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SendMessageImpl
 * @Author Ts
 * @Version 1.0
 */
@Service
//@DubboService(timeout = 3000)
@DubboService(retries = 0)
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MqSendMessageWithLogEnhanceService mqSendMessageWithLogEnhanceService;
    @Autowired
    private MessageSendInterceptorChain messageSendInterceptorChain;


    @Override
    public MessageSendRsp sendMessage(SendMessageRequest messageRequest) {
        //消息类型
        MessageType messageType = MessageType.getByCode(messageRequest.getMessageType());
        try {
            //发送前校验
            messageSendInterceptorChain.intercept(messageRequest, messageType);
        } catch (Exception e) {
            return MessageSendRsp.fail(e.getMessage());
        }
        if (messageRequest.getSingle() == null || messageRequest.getSingle()) {
            //单消息发送
            //包装消息
//            MessageSendPayload messageSendPayload = createMessagePayload(messageRequest, messageRequest.getReceiveAccount());
//            Message<MessageSendPayload> message = MessageBuilder
//                    .withPayload(messageSendPayload)
//                    .build();
            Message<MessageSendPayload> message = createMessage(messageRequest, messageRequest.getReceiveAccount());
            if (YesOrNoEnum.YES.getValue().equals(messageRequest.getRequireAsync())) {
                //异步发送
                return mqSendMessageWithLogEnhanceService.asyncSend(message);
            } else {
                //同步发送
                return mqSendMessageWithLogEnhanceService.syncSend(message);
            }
        } else {
            //批量发送
            //组装消息
            List<Message<MessageSendPayload>> messages = messageRequest.getReceiveAccounts().stream().map(receiveAccount -> {
//                MessageSendPayload messageSendPayload = createMessagePayload(messageRequest, receiveAccount);
//                return MessageBuilder.
//                        withPayload(messageSendPayload)
//                        .build();
                return createMessage(messageRequest, receiveAccount);
            }).collect(Collectors.toList());
            if (YesOrNoEnum.YES.getValue().equals(messageRequest.getRequireAsync())) {
                //异步发送
                return mqSendMessageWithLogEnhanceService.asyncSend(messages);
            } else {
                //同步发送
                return mqSendMessageWithLogEnhanceService.syncSend(messages);
            }
        }
    }


//    private HashMap<String,String> createMessageHeaders(SendMessageRequest messageRequest) {
//        HashMap<String, String> headersMap = new HashMap<>();
    //消息ID
//        headersMap.put(MessageHeaderName.MESSAGE_ID, MessageIdUtils.getMessageId());
    //模板id
//        headersMap.put(MessageHeaderName.TEMPLATE_ID, messageRequest.getTemplateId().toString());
    //平台id
//        headersMap.put(MessageHeaderName.PLATFORM_ID, platformCache.getPlatformId());
    //是否验证重复消费
//        headersMap.put(MessageHeaderName.REQUIRE_REPEAT_CONSUME, templateCache.getRequireRepeatConsume());
    //是否限流
//        headersMap.put(MessageHeaderName.REQUIRE_RATE_LIMIT, templateCache.getRequireRateLimit());
//        if (YesOrNoEnum.YES.getValue().equals(templateCache.getRequireRateLimit())) {
//            //限流配置
//            headersMap.put(MessageHeaderName.RATE_LIMIT_KEY, platformCache.getRateLimitKey());
//            headersMap.put(MessageHeaderName.RATE_LIMIT_SCALE, platformCache.getRateLimitScale().toString());
//            headersMap.put(MessageHeaderName.RATE_LIMIT_INTERVAL, platformCache.getRateLimitInterval().toString());
//        }
    //是否需要异步消费
//        headersMap.put(MessageHeaderName.REQUIRE_ASYNC, templateCache.getRequireAsync());
    //消息类型
//        headersMap.put(MessageHeaderName.MESSAGE_TYPE, messageRequest.getMessageType().toString());
    //消息处理器名
//        headersMap.put(MessageHeaderName.HANDLER_NAME, platformCache.getHandlerName());
//        return headersMap;
//    }


    private MessageSendPayload createMessagePayload(SendMessageRequest messageRequest, String receiveAccount) {
        return new MessageSendPayload(MessageIdUtils.getMessageId(), receiveAccount, messageRequest.getTemplateId(), messageRequest.getSubjectParams(), messageRequest.getContentParams(), messageRequest.getMessageType(), System.currentTimeMillis());
    }

    private Message<MessageSendPayload> createMessage(SendMessageRequest messageRequest, String receiveAccount) {
        return MessageBuilder.
                withPayload(createMessagePayload(messageRequest, receiveAccount))
                .setHeader("forTest", messageRequest.isForTest() ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue())
                .build();
    }


}
