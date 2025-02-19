package com.yt.message.handler.topic.message;

import com.yt.message.handler.handler.chain.MessageHandlerChain;
import com.yt.message.mq.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MessageSendTopicConsumer
 * @Author Ts
 * @Version 1.0
 */
@Service
@RocketMQMessageListener(topic = MQConstant.MESSAGE_TOPIC_NAME,
        consumerGroup = MQConstant.MESSAGE_TOPIC_CONSUMER_GROUP,
        consumeThreadNumber = MQConstant.MESSAGE_TOPIC_CONSUMER_THREAD_NUMBER,
        maxReconsumeTimes = MQConstant.MESSAGE_MAX_RECONSUME_TIMES)
@Slf4j
public class MessageSendTopicConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private MessageHandlerChain messageHandlerChain;


    @Override
    public void onMessage(MessageExt message) {
        messageHandlerChain.handleMessage(message);
    }
}
