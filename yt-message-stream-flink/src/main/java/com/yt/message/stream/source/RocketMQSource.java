package com.yt.message.stream.source;

import com.yt.message.stream.source.enumstate.RocketMQSourceEnumState;
import com.yt.message.stream.source.enumstate.RocketMQSourceEnumStateSerializer;
import com.yt.message.stream.source.enumstate.RocketMQSourceEnumerator;
import com.yt.message.stream.source.enumstate.offset.OffsetsSelector;
import com.yt.message.stream.source.metrics.RocketMQSourceReaderMetrics;
import com.yt.message.stream.source.reader.MessageView;
import com.yt.message.stream.source.reader.RocketMQSplitReader;
import com.yt.message.stream.source.reader.deserializer.RocketMQDeserializationSchema;
import com.yt.message.stream.source.split.RocketMQPartitionSplitSerializer;
import com.yt.message.stream.source.split.RocketMQSourceSplit;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.*;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.util.UserCodeClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.function.Supplier;

/**
 * @ClassName RocketMQStreamSource
 * @Author Ts
 * @Version 1.0
 */

public class RocketMQSource<OUT>  implements Source<OUT, RocketMQSourceSplit, RocketMQSourceEnumState>,
        ResultTypeQueryable<OUT> {

    @Serial
    private static final long serialVersionUID = -1L;
    private static final Logger log = LoggerFactory.getLogger(RocketMQSource.class);

    // Users can specify the starting / stopping offset initializer.
    private final OffsetsSelector startingOffsetsSelector;
    private final OffsetsSelector stoppingOffsetsSelector;

    // The configurations.
    private final Configuration configuration;

    // Boundedness
    private final Boundedness boundedness;

    // RocketMQ DeserializationSchema
    private final RocketMQDeserializationSchema<OUT> deserializationSchema;

    public RocketMQSource(
            OffsetsSelector startingOffsetsSelector,
            OffsetsSelector stoppingOffsetsSelector,
            Boundedness boundedness,
            RocketMQDeserializationSchema<OUT> deserializationSchema,
            Configuration configuration) {
        this.startingOffsetsSelector = startingOffsetsSelector;
        this.stoppingOffsetsSelector = stoppingOffsetsSelector;
        this.boundedness = boundedness;
        this.deserializationSchema = deserializationSchema;
        this.configuration = configuration;
    }


    /**
     * Get a RocketMQSourceBuilder to build a {@link RocketMQSourceBuilder}.
     *
     * @return a RocketMQ source builder.
     */
    public static <OUT> RocketMQSourceBuilder<OUT> builder() {
        return new RocketMQSourceBuilder<>();
    }

    @Override
    public Boundedness getBoundedness() {
        return this.boundedness;
    }

    @Override
    public SourceReader<OUT, RocketMQSourceSplit> createReader(SourceReaderContext readerContext)
            throws Exception {
        deserializationSchema.open(
                new DeserializationSchema.InitializationContext() {
                    @Override
                    public MetricGroup getMetricGroup() {
                        return readerContext.metricGroup().addGroup("deserializer");
                    }

                    @Override
                    public UserCodeClassLoader getUserCodeClassLoader() {
                        return readerContext.getUserCodeClassLoader();
                    }
                });

        final RocketMQSourceReaderMetrics rocketMQSourceReaderMetrics =
                new RocketMQSourceReaderMetrics(readerContext.metricGroup());

        RocketMQSourceFetcherManager rocketmqSourceFetcherManager = getRocketMQSourceFetcherManager(readerContext, rocketMQSourceReaderMetrics);

        RocketMQRecordEmitter<OUT> recordEmitter =
                new RocketMQRecordEmitter<>(deserializationSchema);

        return new RocketMQSourceReader<>(
                rocketmqSourceFetcherManager,
                recordEmitter,
                configuration,
                readerContext,
                rocketMQSourceReaderMetrics);
    }

    private RocketMQSourceFetcherManager getRocketMQSourceFetcherManager(SourceReaderContext readerContext, RocketMQSourceReaderMetrics rocketMQSourceReaderMetrics) {
        Supplier<SplitReader<MessageView, RocketMQSourceSplit>> splitReaderSupplier =
                () ->
                        new RocketMQSplitReader<>(
                                configuration,
                                readerContext,
                                deserializationSchema,
                                rocketMQSourceReaderMetrics);

        return
                new RocketMQSourceFetcherManager(
                        configuration, splitReaderSupplier, (ignore) -> {});

    }

    @Override
    public SplitEnumerator<RocketMQSourceSplit, RocketMQSourceEnumState> createEnumerator(
            SplitEnumeratorContext<RocketMQSourceSplit> enumContext) {

        return new RocketMQSourceEnumerator(
                startingOffsetsSelector,
                stoppingOffsetsSelector,
                boundedness,
                configuration,
                enumContext);
    }

    @Override
    public SplitEnumerator<RocketMQSourceSplit, RocketMQSourceEnumState> restoreEnumerator(
            SplitEnumeratorContext<RocketMQSourceSplit> enumContext,
            RocketMQSourceEnumState checkpoint) {

        return new RocketMQSourceEnumerator(
                startingOffsetsSelector,
                stoppingOffsetsSelector,
                boundedness,
                configuration,
                enumContext,
                checkpoint.currentSplitAssignment());
    }

    @Override
    public SimpleVersionedSerializer<RocketMQSourceSplit> getSplitSerializer() {
        return new RocketMQPartitionSplitSerializer();
    }

    @Override
    public SimpleVersionedSerializer<RocketMQSourceEnumState> getEnumeratorCheckpointSerializer() {
        return new RocketMQSourceEnumStateSerializer();
    }

    @Override
    public TypeInformation<OUT> getProducedType() {
        return deserializationSchema.getProducedType();
    }
}
