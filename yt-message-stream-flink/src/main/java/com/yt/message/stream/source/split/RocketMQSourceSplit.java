package com.yt.message.stream.source.split;

import org.apache.flink.api.connector.source.SourceSplit;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Objects;

import static com.yt.message.stream.util.UtilAll.SEPARATOR;

/**
 * @ClassName RocketMQSourceSplit
 * @Author Ts
 * @Version 1.0
 */

public class RocketMQSourceSplit implements SourceSplit {
    // -1 means Long.MAX_VALUE
    public static final long NO_STOPPING_OFFSET = -1L;

    private final String topic;
    private final String brokerName;
    private final int queueId;
    private final long startingOffset;
    private final long stoppingOffset;

    public RocketMQSourceSplit(
            MessageQueue messageQueue, long startingOffset, long stoppingOffset) {
        this(
                messageQueue.getTopic(),
                messageQueue.getBrokerName(),
                messageQueue.getQueueId(),
                startingOffset,
                stoppingOffset);
    }

    public RocketMQSourceSplit(
            String topic,
            String brokerName,
            int queueId,
            long startingOffset,
            long stoppingOffset) {
        this.topic = topic;
        this.brokerName = brokerName;
        this.queueId = queueId;
        this.startingOffset = startingOffset;
        this.stoppingOffset = stoppingOffset;
    }

    public String getTopic() {
        return topic;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public int getQueueId() {
        return queueId;
    }

    public long getStartingOffset() {
        return startingOffset;
    }

    public long getStoppingOffset() {
        return stoppingOffset;
    }

    public MessageQueue getMessageQueue() {
        return new MessageQueue(topic, brokerName, queueId);
    }

    @Override
    public String splitId() {
        return topic + SEPARATOR + brokerName + SEPARATOR + queueId;
    }

    @Override
    public String toString() {
        return String.format(
                "(Topic: %s, BrokerName: %s, QueueId: %d, MinOffset: %d, MaxOffset: %d)",
                topic, brokerName, queueId, startingOffset, stoppingOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, brokerName, queueId, startingOffset, stoppingOffset);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RocketMQSourceSplit other)) {
            return false;
        }
        return topic.equals(other.topic)
                && brokerName.equals(other.brokerName)
                && queueId == other.queueId
                && startingOffset == other.startingOffset
                && stoppingOffset == other.stoppingOffset;
    }
}
