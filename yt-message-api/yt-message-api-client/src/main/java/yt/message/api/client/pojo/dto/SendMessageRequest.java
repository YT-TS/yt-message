package yt.message.api.client.pojo.dto;

import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.YesOrNoEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @ClassName SendMessageRequest
 * @Author Ts
 * @Version 1.0
 */
@Getter
public class SendMessageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -70348971907999939L;


    /**
     * 下发账户
     */
    private final String receiveAccount;

    /**
     * 下发账户集合
     */
    private final Set<String> receiveAccounts;

    /**
     * 模板id
     */
    protected Long templateId;

    /**
     * 标题参数
     */
    private final String[] subjectParams;

    /**
     * 内容参数
     */
    private final String[] contentParams;
    /**
     * 是否单条发送
     */
    private final Boolean single ;

    /**
     * 消息类型
     */
    private final Integer messageType;

    /**
     * 是否异步发送
     */
    private final Integer requireAsync ;

    @Setter
    @Getter
    private  boolean forTest  = false ;



    private SendMessageRequest(Long templateId, String receiveAccount,Set<String> receiveAccounts, String[] subjectParams,String[] contentParams,Boolean single,Integer messageType, Integer requireAsync) {
        this.single = single;
        this.templateId = templateId;
        this.receiveAccount = receiveAccount;
        this.subjectParams = subjectParams;
        this.contentParams = contentParams;
        this.receiveAccounts = receiveAccounts;
        this.messageType = messageType;
        this.requireAsync = requireAsync;
    }


    private static class BaseBuilder {
        /**
         * 下发账户
         */
        protected String receiveAccount;

        /**
         * 下发账户集合
         */
        protected Set<String> receiveAccounts;

        /**
         * 模板id
         */
        protected Long templateId;

        /**
         * 标题参数
         */
        protected String[] subjectParams;

        /**
         * 内容参数
         */
        protected String[] contentParams;
        /**
         * 是否单条发送
         */
        protected Boolean single ;

        /**
         * 消息类型
         */
        protected Integer messageType;

        protected Integer requireAsync = YesOrNoEnum.NO.getValue();


        private BaseBuilder() {
        }
    }


    public static SendSMSMessageRequestBuilder sendSMSMessageRequestBuilder() {
        return new SendSMSMessageRequestBuilder();
    }
    public static SendEmailMessageRequestBuilder sendEmailMessageRequestBuilder() {
        return new SendEmailMessageRequestBuilder();
    }

    public static SendRobotMessageRequestBuilder sendRobotMessageRequestBuilder() {
        return new SendRobotMessageRequestBuilder();
    }
    public static SendMiniProgramMessageRequestBuilder sendMiniProgramMessageRequestBuilder() {
        return new SendMiniProgramMessageRequestBuilder();
    }

    public static SendWeChatOfficialAccountMessageBuilder sendWeChatOfficialAccountMessageBuilder() {
        return new SendWeChatOfficialAccountMessageBuilder();
    }



    public static class SendSMSMessageRequestBuilder extends BaseBuilder {

        private SendSMSMessageRequestBuilder() {
        }

        public SendSMSMessageRequestBuilder templateId(Long templateId) {

            this.templateId = templateId;
            return this;
        }

        public SendSMSMessageRequestBuilder receiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
            this.single = true;
            return this;
        }
        public SendSMSMessageRequestBuilder receiveAccounts(Set<String> receiveAccounts) {
            this.receiveAccounts = receiveAccounts;
            this.single = false;
            return this;
        }


        public SendSMSMessageRequestBuilder contentParams(String[] contentParams) {
            this.contentParams = contentParams;
            return this;
        }
        public SendSMSMessageRequestBuilder requireAsync(boolean requireAsync) {
            this.requireAsync = requireAsync ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue();
            return this;
        }


        public SendMessageRequest build() {
            return new SendMessageRequest(templateId, receiveAccount,receiveAccounts,null,contentParams,single, MessageType.SMS.getCode(),requireAsync);
        }
    }


    public static class SendEmailMessageRequestBuilder extends BaseBuilder {


        private SendEmailMessageRequestBuilder() {
        }

        public SendEmailMessageRequestBuilder templateId(Long templateId) {
            this.templateId = templateId;
            return this;
        }

        public SendEmailMessageRequestBuilder receiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
            this.single = true;
            return this;
        }
        public SendEmailMessageRequestBuilder receiveAccounts(Set<String> receiveAccounts) {
            this.receiveAccounts = receiveAccounts;
            this.single = false;
            return this;
        }

        public SendEmailMessageRequestBuilder contentParams(String[] contentParams) {
            this.contentParams = contentParams;
            return this;
        }

        public SendEmailMessageRequestBuilder subjectParams(String[] subjectParams) {
            this.subjectParams = subjectParams;
            return this;
        }

        public SendEmailMessageRequestBuilder requireAsync(boolean requireAsync) {
            this.requireAsync = requireAsync ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue();
            return this;
        }
        public SendMessageRequest build() {
            return new SendMessageRequest(templateId, receiveAccount,receiveAccounts,subjectParams,contentParams,single,MessageType.EMAIL.getCode(),requireAsync);
        }
    }


    public static class SendRobotMessageRequestBuilder extends BaseBuilder {
        public SendRobotMessageRequestBuilder templateId(Long templateId) {
            this.templateId = templateId;
            return this;
        }
        public SendRobotMessageRequestBuilder contentParams(String[] contentParams) {
            this.contentParams = contentParams;
            return this;
        }
        public SendRobotMessageRequestBuilder requireAsync(boolean requireAsync) {
            this.requireAsync = requireAsync ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue();
            return this;
        }
        public SendMessageRequest build() {
            return new SendMessageRequest(templateId, null,null,null,contentParams,single,MessageType.ROBOT.getCode(),requireAsync);
        }

    }

    public static class SendMiniProgramMessageRequestBuilder extends BaseBuilder {
        private SendMiniProgramMessageRequestBuilder() {
        }

        public SendMiniProgramMessageRequestBuilder templateId(Long templateId) {

            this.templateId = templateId;
            return this;
        }

        public SendMiniProgramMessageRequestBuilder receiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
            this.single = true;
            return this;
        }
        public SendMiniProgramMessageRequestBuilder receiveAccounts(Set<String> receiveAccounts) {
            this.receiveAccounts = receiveAccounts;
            this.single = false;
            return this;
        }


        public SendMiniProgramMessageRequestBuilder contentParams(String[] contentParams) {
            this.contentParams = contentParams;
            return this;
        }
        public SendMiniProgramMessageRequestBuilder requireAsync(boolean requireAsync) {
            this.requireAsync = requireAsync ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue();
            return this;
        }


        public SendMessageRequest build() {
            return new SendMessageRequest(templateId, receiveAccount,receiveAccounts,null,contentParams,single, MessageType.SMS.getCode(),requireAsync);
        }
    }

    public static class SendWeChatOfficialAccountMessageBuilder extends BaseBuilder{
        private SendWeChatOfficialAccountMessageBuilder() {
        }

        public SendWeChatOfficialAccountMessageBuilder templateId(Long templateId) {

            this.templateId = templateId;
            return this;
        }

        public SendWeChatOfficialAccountMessageBuilder receiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
            this.single = true;
            return this;
        }
        public SendWeChatOfficialAccountMessageBuilder receiveAccounts(Set<String> receiveAccounts) {
            this.receiveAccounts = receiveAccounts;
            this.single = false;
            return this;
        }


        public SendWeChatOfficialAccountMessageBuilder contentParams(String[] contentParams) {
            this.contentParams = contentParams;
            return this;
        }
        public SendWeChatOfficialAccountMessageBuilder requireAsync(boolean requireAsync) {
            this.requireAsync = requireAsync ? YesOrNoEnum.YES.getValue() : YesOrNoEnum.NO.getValue();
            return this;
        }


        public SendMessageRequest build() {
            return new SendMessageRequest(templateId, receiveAccount,receiveAccounts,null,contentParams,single, MessageType.WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE.getCode(),requireAsync);
        }
    }


}
