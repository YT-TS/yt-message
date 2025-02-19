package com.yt.message.admin.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 消息模板
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Template extends  BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 模板id
     */
    @TableId("template_id")
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
    private Integer messageUsage;

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


    private Integer version;



}
