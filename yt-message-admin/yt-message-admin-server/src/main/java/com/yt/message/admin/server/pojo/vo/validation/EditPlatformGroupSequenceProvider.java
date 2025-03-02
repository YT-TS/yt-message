package com.yt.message.admin.server.pojo.vo.validation;


import cn.hutool.core.util.StrUtil;
import com.yt.message.admin.server.pojo.vo.PlatformEditReqVo;
import com.yt.message.admin.server.pojo.vo.validation.group.*;
import com.yt.message.common.enums.MessageType;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class EditPlatformGroupSequenceProvider implements DefaultGroupSequenceProvider<PlatformEditReqVo> {
    @Override
    public List<Class<?>> getValidationGroups(PlatformEditReqVo reqVo) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(PlatformEditReqVo.class); // 这一步不能省,否则Default分组都不会执行了，会抛错的
        if (reqVo != null) { // 这块判空请务必要做
            MessageType messageType = MessageType.getByCode(reqVo.getMessageType());
            if (messageType != null) {
                switch (messageType) {
                    case SMS:
                        defaultGroupSequence.add(SmsGroup.class);
                        break;
                    case EMAIL:
                        defaultGroupSequence.add(EmailGroup.class);
                        break;
                    case ROBOT:
                        defaultGroupSequence.add(RobotGroup.class);
                        break;
                    case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE:
                        defaultGroupSequence.add(WeChatOfficialAccountTemplateMessage.class);
                        break;
                }
            }
            if (StrUtil.isNotEmpty(reqVo.getRateLimitKey())) {
                //限流参数校验
                defaultGroupSequence.add(RequiredRateLimitGroup.class);
            }
        }
        return defaultGroupSequence;
    }
}




