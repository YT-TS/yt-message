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

package com.yt.message.stream.source;

import com.yt.message.stream.source.reader.MessageView;
import com.yt.message.stream.source.reader.RocketMQSplitReader;
import com.yt.message.stream.source.split.RocketMQSourceSplit;
import org.apache.flink.annotation.Internal;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.fetcher.SingleThreadFetcherManager;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcher;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcherTask;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Internal
public class RocketMQSourceFetcherManager
        extends SingleThreadFetcherManager<MessageView, RocketMQSourceSplit> {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQSourceFetcherManager.class);

    /**
     * Creates a new SplitFetcherManager with a single I/O threads.
     *
     * @param splitReaderSupplier The factory for the split reader that connects to the source
     *     system.
     * @param splitFinishedHook Hook for handling finished splits in split fetchers.
     */
    public RocketMQSourceFetcherManager(
            Configuration configuration,
            Supplier<SplitReader<MessageView, RocketMQSourceSplit>> splitReaderSupplier,
            Consumer<Collection<String>> splitFinishedHook) {

        super(splitReaderSupplier, configuration, splitFinishedHook);
    }

    public void commitOffsets(Map<MessageQueue, Long> offsetsToCommit) {
        if (offsetsToCommit.isEmpty()) {
            return;
        }

//        LOG.info("Consumer commit offsets {}", offsetsToCommit);
        SplitFetcher<MessageView, RocketMQSourceSplit> splitFetcher = fetchers.get(0);
        if (splitFetcher != null) {
            // The fetcher thread is still running. This should be the majority of the cases.
            enqueueOffsetsCommitTask(splitFetcher, offsetsToCommit);
        } else {
            splitFetcher = createSplitFetcher();
            enqueueOffsetsCommitTask(splitFetcher, offsetsToCommit);
            startFetcher(splitFetcher);
        }
    }

    private void enqueueOffsetsCommitTask(
            SplitFetcher<MessageView, RocketMQSourceSplit> splitFetcher,
            Map<MessageQueue, Long> offsetsToCommit) {

        RocketMQSplitReader<?> splitReader = (RocketMQSplitReader<?>) splitFetcher.getSplitReader();

        splitFetcher.enqueueTask(
                new SplitFetcherTask() {
                    @Override
                    public boolean run() {
                        splitReader.notifyCheckpointComplete(offsetsToCommit);
                        return true;
                    }

                    @Override
                    public void wakeUp() {}
                });
    }
}
