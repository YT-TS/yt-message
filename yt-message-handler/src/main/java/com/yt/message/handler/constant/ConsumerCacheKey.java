package com.yt.message.handler.constant;

import com.yt.message.common.constant.ICacheKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
@Getter
@AllArgsConstructor
public enum ConsumerCacheKey implements ICacheKey {

    REPEAT_CONSUME("REPEAT_CONSUME", Duration.ofSeconds(60)),


    ;
    private String prefix;

    private Duration expire;



}