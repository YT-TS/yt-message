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

package com.yt.message.stream.source.reader.deserializer;

import com.yt.message.stream.source.reader.MessageView;
import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import java.io.Serial;

/**
 * A {@link RocketMQDeserializationSchema} implementation which based on the given flink's {@link
 * DeserializationSchema}. We would consume the message as byte array from rocketmq and deserialize
 * it by using flink serialization logic.
 *
 * @param <T> The output type of the message.
 */
@Internal
public class RocketMQDeserializationSchemaWrapper<T> implements RocketMQDeserializationSchema<T> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final DeserializationSchema<T> deserializationSchema;

    RocketMQDeserializationSchemaWrapper(DeserializationSchema<T> deserializationSchema) {
        this.deserializationSchema = deserializationSchema;
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        RocketMQDeserializationSchema.super.open(context);
    }

    @Override
    public void deserialize(MessageView messageView, Collector<T> out) {}

    @Override
    public TypeInformation<T> getProducedType() {
        return deserializationSchema.getProducedType();
    }
}
