package com.yt.message.admin.server.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;


/**
 * <p>
 * 预发送的消息模板，可直接用来发送消息
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
@TableName("prepared_template")
public class PreparedTemplate extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId("prepared_template_id")
    private Long preparedTemplateId;

    private String templateName;

    private Long templateId;

    private String subjectParams;


    private String contentParams;


    private String receiveAccounts;


    private Long accountGroupId;

    private Integer requireAsync;

    public Long getPreparedTemplateId() {
        return preparedTemplateId;
    }

    public void setPreparedTemplateId(Long preparedTemplateId) {
        this.preparedTemplateId = preparedTemplateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getSubjectParams() {
        return subjectParams;
    }

    public void setSubjectParams(String subjectParams) {
        this.subjectParams = subjectParams;
    }

    public String getContentParams() {
        return contentParams;
    }

    public void setContentParams(String contentParams) {
        this.contentParams = contentParams;
    }

    public String getReceiveAccounts() {
        return receiveAccounts;
    }

    public void setReceiveAccounts(String receiveAccounts) {
        this.receiveAccounts = receiveAccounts;
    }

    public Long getAccountGroupId() {
        return accountGroupId;
    }

    public void setAccountGroupId(Long accountGroupId) {
        this.accountGroupId = accountGroupId;
    }

    public Integer getRequireAsync() {
        return requireAsync;
    }

    public void setRequireAsync(Integer requireAsync) {
        this.requireAsync = requireAsync;
    }

    @Override
    public String toString() {
        return "PreparedTemplate{" +
            "preparedTemplateId = " + preparedTemplateId +
            ", templateName = " + templateName +
            ", templateId = " + templateId +
            ", subjectParams = " + subjectParams +
            ", contentParams = " + contentParams +
            ", receiveAccounts = " + receiveAccounts +
            ", accountGroupId = " + accountGroupId +
            ", requireAsync = " + requireAsync +
        "}";
    }
}
