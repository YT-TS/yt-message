/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yt.message.stream.source.enumstate.offset;


import com.yt.message.stream.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class OffsetsSelectorByStrategy implements OffsetsSelector, OffsetsValidator {

    private final ConsumeFromWhere consumeFromWhere;
    private final OffsetResetStrategy offsetResetStrategy;

    OffsetsSelectorByStrategy(
            ConsumeFromWhere consumeFromWhere, OffsetResetStrategy offsetResetStrategy) {
        this.consumeFromWhere = consumeFromWhere;
        this.offsetResetStrategy = offsetResetStrategy;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Map<MessageQueue, Long> getMessageQueueOffsets(
            Collection<MessageQueue> messageQueues, MessageQueueOffsetsRetriever offsetsRetriever) {

        return switch (consumeFromWhere) {
            case CONSUME_FROM_FIRST_OFFSET -> offsetsRetriever.minOffsets(messageQueues);
            case CONSUME_FROM_MAX_OFFSET -> offsetsRetriever.maxOffsets(messageQueues);
            default -> offsetsRetriever.committedOffsets(messageQueues);
        };
    }

    @Override
    public OffsetResetStrategy getAutoOffsetResetStrategy() {
        return offsetResetStrategy;
    }

    @Override
    public void validate(Properties rocketmqSourceProperties) throws IllegalStateException {}
}
