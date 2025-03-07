package com.yt.message.admin.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Platform extends  BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台id
     */
    @TableId(value = "platform_id")
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
     * 请求服务器的口
     */
    private Integer port;

    /**
     * 对应的handlerName
     */

    private String handlerName;


    private Integer version;

    /**
     * 状态
     */
    private Integer status;

}
