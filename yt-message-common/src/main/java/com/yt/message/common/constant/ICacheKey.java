package com.yt.message.common.constant;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.time.Duration;

public interface ICacheKey {
    /**
     * 获取前缀
     *
     * @return key 前缀
     */
    String getPrefix();

    /**
     * 超时时间
     *
     * @return 超时时间
     */
     Duration getExpire() ;


    /**
     * 组装 key
     *
     * @param suffix 参数
     * @return cache key
     */
    default String getKey(Object... suffix) {
        String prefix = this.getPrefix();
        // 拼接参数
        if (ArrayUtil.isEmpty(suffix)) {
            return prefix;
        }
        return prefix + CharPool.COLON + StrUtil.join(String.valueOf(CharPool.COLON),suffix);
    }

}
