package com.yt.message.stream;

import cn.hutool.json.JSONUtil;
import com.yt.message.common.pojo.dto.MessageSendResultPlayLoad;
import com.yt.message.mq.constant.MQConstant;
import com.yt.message.stream.legacy.common.config.OffsetResetStrategy;
import com.yt.message.stream.sink.StatisticsSink;
import com.yt.message.stream.source.RocketMQSource;
import com.yt.message.stream.source.RocketMQSourceOptions;
import com.yt.message.stream.source.enumstate.offset.OffsetsSelector;
import com.yt.message.stream.source.reader.MessageView;
import com.yt.message.stream.source.reader.deserializer.RocketMQDeserializationSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class FlinkBootStrap {

    public static void main(String[] args) throws Exception {

        InputStream inputStream =
                FlinkBootStrap.class.getClassLoader().getResourceAsStream("mq.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        env.enableCheckpointing(10000);
        RocketMQSource<String> source = RocketMQSource.<String>builder()
                .setConfig(RocketMQSourceOptions.PARTITION_DISCOVERY_INTERVAL_MS, -1L)
                .setEndpoints(properties.getProperty("name-server"))
                .setTopics(MQConstant.MESSAGE_SEND_RESULT_TOPIC_NAME)
                .setGroupId(MQConstant.MESSAGE_SEND_RESULT_TOPIC_CONSUMER_GROUP)
                .setDeserializer(
                        new RocketMQDeserializationSchema<>() {
                            @Override
                            public void deserialize(
                                    MessageView messageView, Collector<String> out) {
                                out.collect(new String(messageView.getBody()));
                            }
                            @Override
                            public TypeInformation<String> getProducedType() {
                                return BasicTypeInfo.STRING_TYPE_INFO;
                            }
                        })
                .setMinOffsets(OffsetsSelector.committedOffsets(OffsetResetStrategy.LATEST))
                .setPullThreadNums(4)
                .build();
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "rocketmq_source")

                .map((MapFunction<String, MessageSendResultPlayLoad>) value -> JSONUtil.toBean(value, MessageSendResultPlayLoad.class))
//                .returns(MessageSendResultPlayLoad.class)
                .sinkTo(new StatisticsSink());
//        .print();
        env.execute();
    }

}
