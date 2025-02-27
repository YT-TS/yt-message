package com.yt.message.admin.server.pojo.vo;

import lombok.Data;

/**
 * @ClassName PlatformTokenVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class PlatformTokenVo {

    private Long platformId;
    private String platformName;

    private String accessToken;
    //过期时间戳 毫秒
    private Long expireTimestamp;


}
