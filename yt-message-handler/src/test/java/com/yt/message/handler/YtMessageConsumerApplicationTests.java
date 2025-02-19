package com.yt.message.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@SpringBootTest
class YtMessageConsumerApplicationTests {
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        System.out.println(redisTemplate.getClass());
        System.out.println(redisTemplate.opsForValue().setIfAbsent("bbb","aaaa", Duration.ofSeconds(60)));
        System.out.println(redisTemplate.opsForValue().setIfAbsent("bbb","aaaa"));

    }

}
