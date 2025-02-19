package com.yt.message.admin.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName PlatformRspVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class PlatformRspVo {

    /**
     * 平台id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long platformId;

    /**
     * 平台名
     */

    private String platformName;

    /**
     * 这条记录对应消息类型
     */

    private Integer messageType;

    /**
     *限流key
     */
    private String rateLimitKey;
    /**
     * 平台服务限流大小
     */
    private Byte rateLimitScale;

    /**
     * 平台服务限流间隔
     */
    private Integer rateLimitInterval;

    /**
     * 应用id
     */

    private String appId;

    /**
     * 访问id
     */

    private String secretId;

    /**
     * 访问key
     */

    private String secretKey;

    /**
     * 签名内容
     */
    private String signName;

    /**
     * 请求服务器的地址
     */

    private String host;

    /**
     * 请求服务器的端口
     */

    private Integer port;

    /**
     * 对应的handlerName
     */

    private String handlerName;

    private Integer version;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;



}
