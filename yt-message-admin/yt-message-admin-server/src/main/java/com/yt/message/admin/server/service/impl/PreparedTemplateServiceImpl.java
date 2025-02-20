package com.yt.message.admin.server.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.message.admin.server.constant.BusinessConstant;
import com.yt.message.admin.server.constant.AccountType;
import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.mapper.AccountGroupMapper;
import com.yt.message.admin.server.mapper.PlatformMapper;
import com.yt.message.admin.server.mapper.PreparedTemplateMapper;
import com.yt.message.admin.server.mapper.TemplateMapper;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.AccountGroup;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.PreparedTemplate;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.pojo.vo.PreparedTemplatePageReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateRspVo;
import com.yt.message.admin.server.pojo.vo.validation.PreparedTemplateEditReqVo;
import com.yt.message.admin.server.service.IPreparedTemplateService;
import com.yt.message.admin.server.utils.AccountCheckUtils;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.admin.server.utils.PageUtils;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.ResultCodeEnum;
import com.yt.message.common.enums.YesOrNoEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yt.message.api.client.pojo.dto.MessageSendRsp;
import yt.message.api.client.pojo.dto.SendMessageRequest;
import yt.message.api.client.service.MessageService;

import java.util.*;

/**
 * <p>
 * 预发送的消息模板，可直接用来发送消息 服务实现类
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
@Service
@Slf4j
public class PreparedTemplateServiceImpl extends ServiceImpl<PreparedTemplateMapper, PreparedTemplate> implements IPreparedTemplateService {
    @Autowired
    private AccountGroupMapper accountGroupMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @DubboReference( timeout = 5000, retries = 0, lazy = true)
    private MessageService messageService;
    @Autowired
    private PlatformMapper platformMapper;

    @Override
    public PageResult<PreparedTemplateRspVo> page(PreparedTemplatePageReqVo reqVo) {
        IPage<PreparedTemplate> page = new Page<>(reqVo.getPageNum(), reqVo.getPageSize());
        LambdaQueryWrapper<PreparedTemplate> queryWrapper = new LambdaQueryWrapper<PreparedTemplate>()
                .eq(reqVo.getTemplateId() != null, PreparedTemplate::getTemplateId, reqVo.getTemplateId());
        this.page(page, queryWrapper);
        return PageUtils.toPageResult(page, PreparedTemplateRspVo.class);
    }


    @Override
    public boolean existByTemplateId(Long TemplateId) {
        LambdaQueryWrapper<PreparedTemplate> wrapper = new LambdaQueryWrapper<PreparedTemplate>().eq(PreparedTemplate::getTemplateId, TemplateId);
        return this.exists(wrapper);
    }

    @Override
    public boolean existByAccountGroupId(Long accountGroupId) {
        LambdaQueryWrapper<PreparedTemplate> wrapper = new LambdaQueryWrapper<PreparedTemplate>().eq(PreparedTemplate::getAccountGroupId, accountGroupId);
        return this.exists(wrapper);
    }

    @Override
    public void add(PreparedTemplateReqVo reqVo) throws Exception {
        MessageType messageType = MessageType.getByCode(reqVo.getMessageType());
        ExceptionAssert.throwOnFalse(messageType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageType")));
        PreparedTemplate preparedTemplate = new PreparedTemplate();
        if (messageType != MessageType.ROBOT) {
            //接受账户存在性校验
            ExceptionAssert.throwOnFalse(reqVo.getAccountGroupId() != null || StrUtil.isNotBlank(reqVo.getReceiveAccounts()), new IllegalRequestException("接收账户为空"));

            //输入接受账户校验
            if (StrUtil.isNotBlank(reqVo.getReceiveAccounts())) {
                Set<String> accounts = stringSetOptional(reqVo.getReceiveAccounts());
                //接受账户格式校验
                //格式校验
                boolean result = AccountCheckUtils.checkAccounts(AccountType.getByCode(reqVo.getMessageType()), accounts);
                ExceptionAssert.throwOnFalse(result, new BusinessException("接收账户格式错误"));
                preparedTemplate.setReceiveAccounts(listJoin(accounts));
            }
            //账户组校验
            if (reqVo.getAccountGroupId() != null) {
                //接受账户组存在性校验
                AccountGroup accountGroup = accountGroupMapper.selectOne(new LambdaQueryWrapper<AccountGroup>().select(AccountGroup::getGroupId, AccountGroup::getType).eq(AccountGroup::getGroupId, reqVo.getAccountGroupId()));
                ExceptionAssert.throwOnFalse(accountGroup != null, new IllegalRequestException("接受账户组不存在"));
                //账户组格式校验
                ExceptionAssert.throwOnFalse(reqVo.getMessageType().equals(accountGroup.getType()), new BusinessException("接受账户组格式与模板不匹配"));
                preparedTemplate.setAccountGroupId(reqVo.getAccountGroupId());
            }
        }
        //模板存在性校验
        ExceptionAssert.throwOnFalse(templateMapper.exists(new LambdaQueryWrapper<Template>().eq(Template::getTemplateId, reqVo.getTemplateId())), new IllegalRequestException("模板不存在"));


        preparedTemplate.setTemplateName(reqVo.getTemplateName());
        preparedTemplate.setTemplateId(reqVo.getTemplateId());
        if (messageType == MessageType.EMAIL) {
            preparedTemplate.setSubjectParams(listJoin(reqVo.getSubjectParams()));
        }
        preparedTemplate.setContentParams(listJoin(reqVo.getContentParams()));
        preparedTemplate.setRequireAsync(reqVo.getRequireAsync());

        this.save(preparedTemplate);

    }


    @Override
    public void remove(Long templateId) {
        this.removeById(templateId);
    }

    @Override
    public void edit(PreparedTemplateEditReqVo reqVo) throws Exception {
        LambdaUpdateWrapper<PreparedTemplate> preparedTemplateLambdaUpdateWrapper = new LambdaUpdateWrapper<PreparedTemplate>()
                .eq(PreparedTemplate::getPreparedTemplateId, reqVo.getPreparedTemplateId())
                .eq(PreparedTemplate::getTemplateId, reqVo.getTemplateId());
        MessageType messageType = MessageType.getByCode(reqVo.getMessageType());
        ExceptionAssert.throwOnFalse(messageType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageType")));
        if (messageType != MessageType.ROBOT) {
            //接受账户存在性校验
            ExceptionAssert.throwOnFalse(reqVo.getAccountGroupId() != null || StrUtil.isNotBlank(reqVo.getReceiveAccounts()), new IllegalRequestException("接收账户为空"));
            //输入接受账户校验
            Set<String> accounts = null;
            if (StrUtil.isNotBlank(reqVo.getReceiveAccounts())) {
                accounts = stringSetOptional(reqVo.getReceiveAccounts());
                //接受账户格式校验
                //格式校验
                boolean result = AccountCheckUtils.checkAccounts(AccountType.getByCode(reqVo.getMessageType()), accounts);
                ExceptionAssert.throwOnFalse(result, new BusinessException("接收账户格式错误"));

            }
            preparedTemplateLambdaUpdateWrapper.set(PreparedTemplate::getReceiveAccounts, listJoin(accounts));
            //账户组校验
            if (reqVo.getAccountGroupId() != null) {
                //接受账户组存在性校验
                AccountGroup accountGroup = accountGroupMapper.selectOne(new LambdaQueryWrapper<AccountGroup>().select(AccountGroup::getGroupId, AccountGroup::getType).eq(AccountGroup::getGroupId, reqVo.getAccountGroupId()));
                ExceptionAssert.throwOnFalse(accountGroup != null, new IllegalRequestException("接受账户组不存在"));
                //账户组格式校验
                ExceptionAssert.throwOnFalse(reqVo.getMessageType().equals(accountGroup.getType()), new BusinessException("接受账户组格式与模板不匹配"));
            }
            preparedTemplateLambdaUpdateWrapper.set(PreparedTemplate::getAccountGroupId, reqVo.getAccountGroupId());
        }
        preparedTemplateLambdaUpdateWrapper.set(PreparedTemplate::getTemplateName, reqVo.getTemplateName())
                .set(PreparedTemplate::getRequireAsync, reqVo.getRequireAsync())
                .set(PreparedTemplate::getContentParams, listJoin(reqVo.getContentParams()));
        if (messageType == MessageType.EMAIL){
            preparedTemplateLambdaUpdateWrapper.set(PreparedTemplate::getSubjectParams, listJoin(reqVo.getSubjectParams()));
        }
        this.update(preparedTemplateLambdaUpdateWrapper);

    }

    @Override
    public void send(Long preparedTemplateId) throws Exception {
        PreparedTemplate preparedTemplate = this.getById(preparedTemplateId);
        ExceptionAssert.throwOnFalse(preparedTemplate != null, new IllegalRequestException("模板不存在"));

        Template template = templateMapper.selectById(preparedTemplate.getTemplateId());
        Platform platform = platformMapper.selectById(template.getPlatformId());


        MessageType messageType = MessageType.getByCode(platform.getMessageType());

        SendMessageRequest request;
        String[] contentParams = stringArrayOptional(preparedTemplate.getContentParams());
        Set<String> receiveAccounts = new HashSet<>(stringSetOptional(preparedTemplate.getReceiveAccounts()));

        Long accountGroupId = preparedTemplate.getAccountGroupId();
        if (accountGroupId != null) {
            AccountGroup accountGroup = accountGroupMapper.selectById(accountGroupId);
            receiveAccounts.addAll(
                    stringSetOptional(accountGroup.getMembers())
            );
        }
        request = switch (messageType) {
            case SMS -> SendMessageRequest.sendSMSMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case EMAIL -> SendMessageRequest.sendEmailMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .subjectParams(stringArrayOptional(preparedTemplate.getSubjectParams()))
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case ROBOT -> SendMessageRequest.sendRobotMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case MINI_PROGRAM -> SendMessageRequest.sendMiniProgramMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
        };
        MessageSendRsp messageSendRsp = messageService.sendMessage(request);
        ExceptionAssert.throwOnFalse(messageSendRsp.getCode() == ResultCodeEnum.SUCCESS.getCode(), new BusinessException(messageSendRsp.getMsg()));

    }

    @Override
    public void sendForTest(Long preparedTemplateId) throws Exception {
        PreparedTemplate preparedTemplate = this.getById(preparedTemplateId);
        ExceptionAssert.throwOnFalse(preparedTemplate != null, new IllegalRequestException("模板不存在"));

        Template template = templateMapper.selectById(preparedTemplate.getTemplateId());
        Platform platform = platformMapper.selectById(template.getPlatformId());


        MessageType messageType = MessageType.getByCode(platform.getMessageType());

        SendMessageRequest request;
        String[] contentParams = stringArrayOptional(preparedTemplate.getContentParams());
        Set<String> receiveAccounts = new HashSet<>(stringSetOptional(preparedTemplate.getReceiveAccounts()));

        Long accountGroupId = preparedTemplate.getAccountGroupId();
        if (accountGroupId != null) {
            AccountGroup accountGroup = accountGroupMapper.selectById(accountGroupId);
            receiveAccounts.addAll(
                    stringSetOptional(accountGroup.getMembers())
            );
        }
        request = switch (messageType) {
            case SMS -> SendMessageRequest.sendSMSMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case EMAIL -> SendMessageRequest.sendEmailMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .subjectParams(stringArrayOptional(preparedTemplate.getSubjectParams()))
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case ROBOT -> SendMessageRequest.sendRobotMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
            case MINI_PROGRAM -> SendMessageRequest.sendMiniProgramMessageRequestBuilder()
                    .templateId(template.getTemplateId())
                    .contentParams(contentParams)
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(preparedTemplate.getRequireAsync()))
                    .build();
        };
        request.setForTest(true);
        MessageSendRsp messageSendRsp = messageService.sendMessage(request);
        ExceptionAssert.throwOnFalse(messageSendRsp.getCode() == ResultCodeEnum.SUCCESS.getCode(), new BusinessException(messageSendRsp.getMsg()));

    }


    private Set<String> stringSetOptional(String options) {
        return Optional.ofNullable(options)
                .map(s -> s.split(BusinessConstant.LIST_SEPARATOR))
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }

    private String[] stringArrayOptional(String options) {
        return Optional.ofNullable(options)
                .map(s -> s.split(BusinessConstant.LIST_SEPARATOR))
                .orElseGet(() -> new String[]{});
    }


//    private <T> T stringSplit(String value, Supplier<T> other, Function<String[], T>... stringArrayMappers) {
//        Optional<String[]> optional = Optional.ofNullable(value)
//                .map(s -> s.split(BusinessConstant.LIST_SEPARATOR));
//
//        List<Function<String[], T>> mappers = stringArrayMappers == null ? Collections.emptyList() : Arrays.asList(stringArrayMappers);
//        for (Function<String[], T> mapper : mappers) {
//            optional.map(mapper);
//        }
//        return optional.orElseGet(other);
//    }

    private String listJoin(Collection<String> list) {

        return CollUtil.isEmpty(list) ? null : StrUtil.join(BusinessConstant.LIST_SEPARATOR, list);
    }


}
