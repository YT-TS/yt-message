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

package com.yt.message.stream.source.enumstate.allocate;

import com.yt.message.stream.source.RocketMQSourceOptions;
import com.yt.message.stream.source.enumstate.RocketMQSourceEnumState;
import com.yt.message.stream.source.split.RocketMQSourceSplit;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.configuration.Configuration;


public class AllocateStrategyFactory {

    public static final String STRATEGY_NAME_BROADCAST = "broadcast";
    public static final String STRATEGY_NAME_CONSISTENT_HASH = "hash";

    private AllocateStrategyFactory() {
        // No public constructor.
    }

    public static AllocateStrategy getStrategy(
            Configuration rocketmqSourceOptions,
            SplitEnumeratorContext<RocketMQSourceSplit> context,
            RocketMQSourceEnumState enumState) {

        String allocateStrategyName =
                rocketmqSourceOptions.get(
                        RocketMQSourceOptions.ALLOCATE_MESSAGE_QUEUE_STRATEGY);

        return switch (allocateStrategyName) {
            case STRATEGY_NAME_CONSISTENT_HASH -> new ConsistentHashAllocateStrategy();
            case STRATEGY_NAME_BROADCAST -> new BroadcastAllocateStrategy();
            default -> throw new IllegalArgumentException(
                    "We don't support this allocate strategy: " + allocateStrategyName);
        };
    }
}
