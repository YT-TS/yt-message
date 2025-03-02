package com.yt.message.admin.server.pojo.vo.validation;

import com.yt.message.admin.server.pojo.vo.TemplateReqVo;
import com.yt.message.admin.server.pojo.vo.validation.group.EmailGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.RobotGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.SmsGroup;
import com.yt.message.admin.server.pojo.vo.validation.group.WeChatOfficialAccountTemplateMessage;
import com.yt.message.common.enums.MessageType;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EditTemplateGroupSequenceProvider
 * @Author Ts
 * @Version 1.0
 */

public class AddTemplateGroupSequenceProvider implements DefaultGroupSequenceProvider<TemplateReqVo> {
    @Override
    public List<Class<?>> getValidationGroups(TemplateReqVo reqVo) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(TemplateReqVo.class); // 这一步不能省,否则Default分组都不会执行了，会抛错的
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
//                    case MINI_PROGRAM:
//                        defaultGroupSequence.add(MiniProgramGroup.class);
//                        break;
                    case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE:
                        defaultGroupSequence.add(WeChatOfficialAccountTemplateMessage.class);
                        break;
                }
            }
        }
        return defaultGroupSequence;
    }
}
