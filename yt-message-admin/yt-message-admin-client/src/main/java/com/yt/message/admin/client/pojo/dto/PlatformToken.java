package com.yt.message.admin.client.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName PlatformToken
 * @Author Ts
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private Long platformId;

    private String accessToken;
    //过期时间戳 毫秒
    private Long expireTimestamp;
    //有效时长 单位s
    private Long lifespan;
}
