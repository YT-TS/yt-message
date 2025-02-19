package com.yt.message.stream.source.enumstate;

import org.apache.flink.annotation.Internal;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Set;

@Internal
public record RocketMQSourceEnumState(Set<MessageQueue> currentSplitAssignment) {

}
