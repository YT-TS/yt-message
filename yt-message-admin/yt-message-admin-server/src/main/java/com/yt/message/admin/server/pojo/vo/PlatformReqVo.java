package com.yt.message.admin.server.pojo.vo;

import com.yt.message.admin.server.pojo.vo.validation.AddPlatformGroupSequenceProvider;
import com.yt.message.admin.server.pojo.vo.validation.group.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;

/**
 * @ClassName PlatformReqVo
 * @Author Ts
 * @Version 1.0
 */
@Data
@GroupSequenceProvider(AddPlatformGroupSequenceProvider.class)
@AllArgsConstructor
@NoArgsConstructor
public class PlatformReqVo  {

    /**
     * 平台名
     */
    @NotBlank(message = "平台名不能为空")
    private String platformName;

    /**
     * 这条记录对应消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

    /**
     * 限流key
     */

    private String rateLimitKey;

    /**
     * 平台服务限流大小
     */
    @NotNull(groups = RequiredRateLimitGroup.class,message = "限流大小不能为空")
    private Integer rateLimitScale;

    /**
     * 平台服务限流间隔
     */
    @NotNull(groups = RequiredRateLimitGroup.class,message = "限流间隔不能为空" )
    private Integer rateLimitInterval;





    /**
     * 应用id
     */
    @NotBlank(groups = {SmsGroup.class, WeChatOfficialAccountTemplateMessage.class},message = "应用id不能为空")
    private String appId;

    /**
     * 访问id
     */
    @NotBlank(groups = SmsGroup.class,message = "访问id不能为空")
    private String secretId;

    /**
     * 访问key
     */
    @NotBlank(groups = {SmsGroup.class, WeChatOfficialAccountTemplateMessage.class},message = "访问key不能为空")
    private String secretKey;

    /**
     * 签名内容
     */
    private String signName;

    /**
     * 请求服务器的地址
     */
    @NotBlank(groups = {EmailGroup.class, SmsGroup.class, RobotGroup.class}, message = "请求服务器的地址不能为空")
    private String host;

    /**
     * 请求服务器的端口
     */
    @NotNull(groups = {EmailGroup.class, SmsGroup.class},message = "请求服务器的端口不能为空")
    private Integer port;

    /**
     * 对应的handlerName
     */
    @NotBlank(message = "处理器名不能为空")
    private String handlerName;

}


