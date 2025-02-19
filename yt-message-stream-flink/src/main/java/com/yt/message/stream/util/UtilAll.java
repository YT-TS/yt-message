package com.yt.message.stream.util;

import com.yt.message.stream.source.split.RocketMQSourceSplit;
import org.apache.rocketmq.common.message.MessageQueue;

public class UtilAll {

    public static final String SEPARATOR = "#";

    public static String getSplitId(MessageQueue mq) {
        return mq.getTopic() + SEPARATOR + mq.getBrokerName() + SEPARATOR + mq.getQueueId();
    }

    public static String getQueueDescription(MessageQueue mq) {
        return String.format(
                "(Topic: %s, BrokerName: %s, QueueId: %d)",
                mq.getTopic(), mq.getBrokerName(), mq.getQueueId());
    }

    public static MessageQueue getMessageQueue(RocketMQSourceSplit split) {
        return new MessageQueue(split.getTopic(), split.getBrokerName(), split.getQueueId());
    }
}