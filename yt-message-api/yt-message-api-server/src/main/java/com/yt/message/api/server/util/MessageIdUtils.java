package com.yt.message.api.server.util;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName MessageIdUtils
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class MessageIdUtils {
    private static  Snowflake SNOWFLAKE ;

    static {
        SNOWFLAKE = BeanLoaderUtils.getSpringBean(Snowflake.class);
    }
    public static Long getMessageId(){
        return SNOWFLAKE.nextId();
//        return System.currentTimeMillis() % 3 ;
    }



}
