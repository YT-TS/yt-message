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
import org.apache.flink.annotation.Internal;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Internal
public class OffsetsSelectorNoStopping implements OffsetsSelector {

    @Override
    public Map<MessageQueue, Long> getMessageQueueOffsets(
            Collection<MessageQueue> messageQueues, MessageQueueOffsetsRetriever offsetsRetriever) {

        return Collections.emptyMap();
    }

    @Override
    public OffsetResetStrategy getAutoOffsetResetStrategy() {

        throw new UnsupportedOperationException(
                "The OffsetsSelectorNoStopping does not have an OffsetResetStrategy. "
                        + "It should only be used to end offset.");
    }
}
