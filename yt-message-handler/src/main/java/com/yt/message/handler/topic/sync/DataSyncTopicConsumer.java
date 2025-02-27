package com.yt.message.handler.topic.sync;

import cn.hutool.json.JSONUtil;
import com.yt.message.handler.util.PlatformTokenUtil;
import com.yt.message.mq.constant.MQConstant;
import com.yt.message.common.pojo.dto.DataSyncMessagePayload;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName DataSyncTopicConsumer
 * @Description 本地缓存同步消费者
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = MQConstant.DATA_SYNC_TOPIC_NAME,
        messageModel = MessageModel.BROADCASTING,
        consumerGroup = MQConstant.DATA_SYNC_TOPIC_CONSUMER_GROUP,
        consumeThreadNumber = MQConstant.DATA_SYNC_TOPIC_CONSUMER_THREAD_NUMBER,
        maxReconsumeTimes = MQConstant.DATA_SYNC_MAX_RECONSUME_TIMES)
public class DataSyncTopicConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        log.info("本地缓存同步");
        DataSyncMessagePayload payload = JSONUtil.toBean(new String(message.getBody()), DataSyncMessagePayload.class);
        switch (payload.getType()){
            case PLATFORM_DELETE:
            case PLATFORM_UPDATE:
                MessageLocalCacheUtil.removePlatform(payload.getId());
                break;
            case TEMPLATE_DELETE:
            case TEMPLATE_UPDATE:
                MessageLocalCacheUtil.removeTemplate(payload.getId());
                break;
            case PLATFORM_TOKEN_UPDATE:
                log.info("删除本地平台token缓存");
                PlatformTokenUtil.removePlatformToken(payload.getId());
                break;
        }

    }
}
