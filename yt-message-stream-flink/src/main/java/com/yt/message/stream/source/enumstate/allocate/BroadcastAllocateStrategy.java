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


import com.yt.message.stream.source.split.RocketMQSourceSplit;

import java.util.*;

public class BroadcastAllocateStrategy implements AllocateStrategy {

    @Override
    public String getStrategyName() {
        return AllocateStrategyFactory.STRATEGY_NAME_BROADCAST;
    }

    @Override
    public Map<Integer, Set<RocketMQSourceSplit>> allocate(
            final Collection<RocketMQSourceSplit> mqAll, final int parallelism) {
        Map<Integer, Set<RocketMQSourceSplit>> result = new HashMap<>(parallelism);
        for (int i = 0; i < parallelism; i++) {
            result.computeIfAbsent(i, k -> new HashSet<>()).addAll(mqAll);
        }
        return result;
    }
}
