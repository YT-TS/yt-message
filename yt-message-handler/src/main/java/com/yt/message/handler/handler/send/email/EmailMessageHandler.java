package com.yt.message.handler.handler.send.email;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.MessageSendPayload;
import com.yt.message.handler.handler.send.MessageHandler;
import com.yt.message.handler.pojo.dto.MessageSendResult;
import com.yt.message.handler.util.MessageLocalCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * @ClassName EmailMessageHandler
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@Component
public class EmailMessageHandler implements MessageHandler {


    @Override
    public MessageSendResult handle(MessageExt message, MessageSendPayload messageSendPayload) {


        log.info("邮件消息处理:" + messageSendPayload.getMessageId());
        TemplateCache templateCache = MessageLocalCacheUtil.getTemplate(messageSendPayload.getTemplateId());
        PlatformCache platformCache = MessageLocalCacheUtil.getPlatform(templateCache.getPlatformId());


        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(platformCache.getHost());
        mailAccount.setSslEnable(YesOrNoEnum.YES.getValue().equals(templateCache.getRequireSsl()));
        mailAccount.setPort(platformCache.getPort());
        mailAccount.setFrom(templateCache.getSendAccount());
        if (StrUtil.isNotEmpty(templateCache.getUsername()) && StrUtil.isNotEmpty(templateCache.getPassword())) {
            mailAccount.setAuth(true);
            mailAccount.setUser(templateCache.getUsername());
            mailAccount.setPass(templateCache.getPassword());
        }

        String subject = templateCache.getSubject();
        if (ArrayUtil.isNotEmpty(messageSendPayload.getSubjectParams())) {
            subject = StrUtil.format(templateCache.getSubject(), (Object[]) messageSendPayload.getSubjectParams());
        }
        String content = templateCache.getContent();

        if (ArrayUtil.isNotEmpty(messageSendPayload.getContentParams())) {
            content = StrUtil.format(templateCache.getContent(), (Object[]) messageSendPayload.getContentParams());
        }


        String sendId = MailUtil.send(mailAccount, messageSendPayload.getReceiveAccount(), subject, content, YesOrNoEnum.YES.getValue().equals(templateCache.getRequireHtml()));
        return StrUtil.isNotEmpty(sendId) ? MessageSendResult.success() : MessageSendResult.fail("邮件发送失败");
    }

    @Override
    public String getName() {
        return "emailHandler";
    }
}
