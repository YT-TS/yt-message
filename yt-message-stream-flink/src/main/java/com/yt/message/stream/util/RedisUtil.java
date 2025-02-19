package com.yt.message.stream.util;

import cn.hutool.core.util.StrUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @ClassName RedisUtil
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class RedisUtil {


    private static RedisClient redisClient;

    static {
        InputStream resourceAsStream = RedisUtil.class.getClassLoader().getResourceAsStream("redis.properties");
        Properties redisProperties = new Properties();
        try {
            redisProperties.load(resourceAsStream);
            RedisURI.Builder builder = RedisURI.Builder.redis(redisProperties.getProperty("host"))
                    .withPort(Integer.parseInt(redisProperties.getProperty("port")));
            String password = redisProperties.getProperty("password");
            if (StrUtil.isNotBlank(password)) {
                builder.withPassword(redisProperties.getProperty("password").toCharArray());
            }

            redisClient = RedisClient.create(builder.build());

        } catch (IOException e) {
            log.error("redis.properties加载失败", e);
        }


    }



    public static void hSet(String key, String field, String value) {
        StatefulRedisConnection<String, String> connect = getConnect();
        connect.async().hset(key, field, value);
        close(connect);

    }

    public static void hSet(String key, Map<String, String> hash) {
        StatefulRedisConnection<String, String> connect = getConnect();
        redisClient.connect().async().hset(key, hash);
        close(connect);


    }

    public static void hIncrementBy(String key, String field, long amount) {
        StatefulRedisConnection<String, String> connect = getConnect();
        redisClient.connect().async().hincrby(key, field, amount);
        close(connect);


    }

    public static void pipeline(Consumer<RedisAsyncCommands<String, String> > consumer) {
        StatefulRedisConnection<String, String> connect = getConnect();
        connect.setAutoFlushCommands(false);
        consumer.accept(connect.async());
        connect.flushCommands();
        close(connect);
    }

    private static StatefulRedisConnection<String, String> getConnect() {
        return redisClient.connect();
    }
    private static void close (StatefulConnection<?, ?> connection) {
        connection.closeAsync();
    }

}
