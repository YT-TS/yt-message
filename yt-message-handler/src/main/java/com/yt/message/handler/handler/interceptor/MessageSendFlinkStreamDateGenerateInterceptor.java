package com.yt.message.handler.handler.interceptor;

import com.yt.message.common.enums.ResultCodeEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.common.pojo.dto.MessageSendResultPlayLoad;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.mq.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @ClassName MessageSendFlinkStreamDateGenerateInterceptor
 * @Description 为flink生成流数据
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@Component
public class MessageSendFlinkStreamDateGenerateInterceptor implements MessageHandlerInterceptor {

    private final RocketMQTemplate rocketMQTemplate;
    public MessageSendFlinkStreamDateGenerateInterceptor(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public void finalHandle(MessageExt message, MessageSendPayload messageSendPayload, @Nullable MessageSendResult result, @Nullable Exception ex) {
        MessageSendResultPlayLoad playLoad = new MessageSendResultPlayLoad
                (messageSendPayload.getMessageId(), result == null ? ResultCodeEnum.FAIL.getCode() : result.getCode(), messageSendPayload.getTimestamp(), messageSendPayload.getMessageType());
        playLoad.setTemplateId(messageSendPayload.getTemplateId());
        playLoad.setPlatformId(messageSendPayload.getPlatformId());
        rocketMQTemplate.asyncSend(MQConstant.MESSAGE_SEND_RESULT_TOPIC_NAME, playLoad, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable e) {
                log.error("消息结果发送队列失败",e);
            }
        });

    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 1;
    }

}
