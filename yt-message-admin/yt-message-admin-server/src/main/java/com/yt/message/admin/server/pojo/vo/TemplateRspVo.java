package com.yt.message.admin.server.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName TemplateRspVo
 * @Author Ts
 * @Version 1.0
 */
@Data
public class TemplateRspVo {

    /**
     * 模板id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long templateId;

    /**
     * 模板名
     */
    private String templateName;

    /**
     * 平台id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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

    private Integer version;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
