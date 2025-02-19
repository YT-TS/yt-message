package com.yt.message.stream.source.enumstate.offset;

import org.apache.flink.annotation.Internal;

import java.util.Properties;

@Internal
public interface OffsetsValidator {

    /**
     * Validate offsets initializer with properties of RocketMQ source.
     *
     * @param properties Properties of RocketMQ source
     * @throws IllegalStateException if validation fails
     */
    void validate(Properties properties) throws IllegalStateException;
}
