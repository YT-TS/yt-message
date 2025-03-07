package com.yt.message.admin.client.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@Data
public class PlatformCache implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 平台id
     */
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
    private Integer rateLimitScale;

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

    private Integer status;

}
