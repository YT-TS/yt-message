package com.yt.message.admin.server.pojo.vo;

import com.yt.message.admin.server.pojo.vo.validation.AddTemplateGroupSequenceProvider;
import com.yt.message.admin.server.pojo.vo.validation.group.EmailGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.MiniProgramGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.RobotGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.SmsGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;



/**
 * @ClassName TemplateEditReqVo
 * @Author Ts
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@GroupSequenceProvider(AddTemplateGroupSequenceProvider.class)
public class TemplateReqVo {

    /**
     * 模板名
     */
    @NotBlank(message = "模板名不能为空")
    private String templateName;

    /**
     * 这条记录对应消息类型
     */
    @NotNull(message = "消息用途不能为空")
    private Integer messageUsage;

    /**
     * 平台id
     */
    @NotNull(message = "平台id不能为空")
    private Long platformId;

    /**
     * 平台的消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;


    /**
     * 对应第三方平台模板id
     */
    @NotBlank(groups = SmsGroup.class, message = "第三方平台模板id不能为空")
    private String platformTemplateId;


    /**
     * 是否需要持久化
     */
    @NotNull(message = "是否需要持久化不能为空")
    private Integer requirePersist;

    /**
     * 是否需要验证重复消费
     */
    @NotNull(message = "是否需要验证重复消费不能为空")
    private Integer requireRepeatConsume;


    /**
     * 是否需要限流
     */
    @NotNull(message = "是否需要限流不能为空")
    private Integer requireRateLimit;

    /**
     * 平台服务限流策略
     */
    private Integer rateLimitStrategy;

    /**
     * 发送账户
     */
    @NotBlank(groups = EmailGroup.class, message = "发送账户不能为空")
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
    @NotNull(groups = EmailGroup.class, message = "是否需要ssl发送不能为空")
    private Integer requireSsl;


    /**
     * 标题(可包含占位符{})
     */
    @NotBlank(groups = EmailGroup.class, message = "标题不能为空")
    private String subject;


    /**
     * 正文(可包含占位符{})
     */
    @NotBlank(groups = {EmailGroup.class, RobotGroup.class},message = "正文不能为空")
    private String content;

    /**
     * 正文内容是否是html格式
     */
    @NotNull(groups = EmailGroup.class, message = "正文内容是否是html格式不能为空")
    private Integer requireHtml;

    /**
     * 小程序的跳转页面地址
     */
    @NotBlank(groups = MiniProgramGroup.class, message = "小程序的跳转页面地址不能为空")
    private String page;
}
