package com.yt.message.admin.client.pojo.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 消息模板
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@Data
public class TemplateCache implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 对应第三方平台模板id
     */
    private String platformTemplateId;

    /**
     * message_usage
     */
    private Byte messageUsage;

    /**
     * 是否需要持久化
     */
    private Integer requirePersist;

    /**
     * 是否需要验证重复消费
     */
    private Integer requireRepeatConsume;

    /**
     * 是否需要限流
     */

    private Integer requireRateLimit;
    /**
     * 平台服务限流策略
     */
    private Integer rateLimitStrategy;


    /**
     * 发送账户
     */

    private String sendAccount;

    /**
     * 用户名
     */

    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否需要ssl发送
     */

    private Integer requireSsl;


    /**
     * 标题(可包含占位符{})
     */

    private String subject;


    /**
     * 正文(可包含占位符{})
     */

    private String content;

    /**
     * 正文内容是否是html格式
     */

    private Integer requireHtml;

    /**
     * 小程序订阅消息跳转小程序的页面地址
     * 微信公众号模板消息跳转小程序的页面地址或者外部url链接
     */
    private String page;
}
