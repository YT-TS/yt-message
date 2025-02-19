package com.yt.message.admin.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName TestController
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/log")
    public void log(){
        log.error("系统异常",new RuntimeException("异常dsadasfasfa"));

    }
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @GetMapping("1")
    public void test1(){
        redisTemplate.opsForHash().increment("test","test",1);
    }
    @GetMapping("2")
    public void test2(){
        HashOperations<String, String, Long>  hashOperations = redisTemplate.opsForHash();
        Map<String, Long> test = hashOperations.entries("test");
        Long test1 = test.get("test");
        log.info("test2:{}",test);
    }

}
