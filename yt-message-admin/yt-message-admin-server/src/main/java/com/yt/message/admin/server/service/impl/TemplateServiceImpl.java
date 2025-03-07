package com.yt.message.admin.server.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.message.admin.server.constant.AccountType;
import com.yt.message.admin.server.constant.BusinessConstant;
import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.mapper.AccountGroupMapper;
import com.yt.message.admin.server.mapper.PlatformMapper;
import com.yt.message.admin.server.mapper.TemplateMapper;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.OneLayerTreeDic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.AccountGroup;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.pojo.vo.*;
import com.yt.message.admin.server.service.IPreparedTemplateService;
import com.yt.message.admin.server.service.ITemplateService;
import com.yt.message.admin.server.utils.AccountCheckUtils;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.admin.server.utils.PageUtils;
import com.yt.message.common.enums.*;
import com.yt.message.common.pojo.dto.DataSyncMessagePayload;
import com.yt.message.mq.constant.MQConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yt.message.api.client.pojo.dto.MessageSendRsp;
import yt.message.api.client.pojo.dto.SendMessageRequest;
import yt.message.api.client.service.MessageService;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息模板 服务实现类
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements ITemplateService {

    @Autowired
    private PlatformMapper platformMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private IPreparedTemplateService preparedTemplateService;

    @DubboReference(timeout = 5000, retries = 0, lazy = true)
    private MessageService messageService;
    @Autowired
    private AccountGroupMapper accountGroupMapper;

    @Override
    public boolean existByPlatformId(Long platformId) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<Template>().eq(Template::getPlatformId, platformId);
        return this.exists(wrapper);
    }

    @Override
    public PageResult<TemplateRspVo> page(TemplatePageReqVo reqVo) {
        IPage<Template> page = new Page<>(reqVo.getPageNum(), reqVo.getPageSize());
        LambdaQueryWrapper<Template> queryWrapper = new LambdaQueryWrapper<Template>()
                .eq(reqVo.getMessageUsage() != null, Template::getMessageUsage, reqVo.getMessageUsage())
                .eq(reqVo.getPlatformId() != null, Template::getPlatformId, reqVo.getPlatformId());
        this.page(page, queryWrapper);
        return PageUtils.toPageResult(page, TemplateRspVo.class);
    }

    @Override
    @Transactional
    public void edit(TemplateEditReqVo reqVo) throws Exception {

        //校验参数
        MessageUsage messageUsage = MessageUsage.getByKey(reqVo.getMessageUsage());
        ExceptionAssert.throwOnFalse(messageUsage != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageUsage")));



        LambdaUpdateWrapper<Template> updateWrapper = new LambdaUpdateWrapper<Template>()
                .eq(Template::getTemplateId, reqVo.getTemplateId())
                .eq(Template::getPlatformId, reqVo.getPlatformId())
                .set(Template::getTemplateName, reqVo.getTemplateName())
                .set(Template::getMessageUsage, reqVo.getMessageUsage())
                .set(Template::getRequirePersist, reqVo.getRequirePersist())
                .set(Template::getRequireRepeatConsume, reqVo.getRequireRepeatConsume())
                .set(Template::getRequireRateLimit, reqVo.getRequireRateLimit())
                .set(Template::getVersion, reqVo.getVersion() + 1);
        if (YesOrNoEnum.NO.getValue().equals(reqVo.getRequireRateLimit())){
            updateWrapper.set(Template::getRateLimitStrategy, null);
        }else{
            //需要限流 检查限流策略
            ExceptionAssert.throwOnFalse(RateLimitStrategy.getByCode(reqVo.getRateLimitStrategy()) != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "rateLimitStrategy")));
            updateWrapper.set(Template::getRateLimitStrategy, reqVo.getRateLimitStrategy());
        }

        MessageType messageType  = Optional.ofNullable( MessageType.getByCode(reqVo.getMessageType()))
                .orElseThrow(() -> new IllegalRequestException("非法参数；messageType"));
        switch (messageType) {
            case SMS:
                updateWrapper.set(Template::getPlatformTemplateId, reqVo.getPlatformTemplateId());
                break;
            case EMAIL:
                updateWrapper.set(Template::getSendAccount, reqVo.getSendAccount())
                        .set(Template::getUsername, reqVo.getUsername())
                        .set(Template::getPassword, reqVo.getPassword())
                        .set(Template::getRequireSsl, reqVo.getRequireSsl())
                        .set(Template::getSubject, reqVo.getSubject())
                        .set(Template::getRequireHtml, reqVo.getRequireHtml())
                        .set(Template::getContent, reqVo.getContent());
                break;
            case ROBOT:
                updateWrapper.set(Template::getContent, reqVo.getContent());
                break;
//            case MINI_PROGRAM:
//                updateWrapper.set(Template::getPage, reqVo.getPage());
//                updateWrapper.set(Template::getPlatformTemplateId, reqVo.getPlatformTemplateId());
//                break;
            case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE:
                updateWrapper.set(Template::getPlatformTemplateId, reqVo.getPlatformTemplateId());
                break;
            default:
                throw new BusinessException("不支持的消息类型");
        }
        ExceptionAssert.throwOnFalse(!existByTemplateNameAndId(reqVo.getTemplateName(), reqVo.getTemplateId()), new BusinessException("模板名已存在"));

        //更新数据，同时同步本地缓存
        DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.TEMPLATE_UPDATE, reqVo.getTemplateId(), reqVo.getVersion());
        Supplier<Boolean> supplier = () -> this.update(updateWrapper);
        rocketMQTemplate.sendMessageInTransaction(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build(), supplier);
        //删除模板名缓存
        StatisticsService.deleteTemplateNameCache(reqVo.getPlatformId());


    }

    @Override
    public void add(TemplateReqVo reqVo) throws Exception {
        //校验参数
        MessageUsage messageUsage = MessageUsage.getByKey(reqVo.getMessageUsage());
        ExceptionAssert.throwOnFalse(messageUsage != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageUsage")));

        //校验平台存在性
        LambdaQueryWrapper<Platform> platformLambdaQueryWrapper = new LambdaQueryWrapper<Platform>()
                .eq(Platform::getPlatformId, reqVo.getPlatformId())
                .eq(Platform::getMessageType, reqVo.getMessageType());


        Platform platform = Optional.ofNullable(platformMapper.selectOne(platformLambdaQueryWrapper))
                .orElseThrow(() -> new IllegalRequestException("非法参数 ：platformId,value " + reqVo.getPlatformId()));
        ExceptionAssert.throwOnFalse(YesOrNoEnum.YES.getValue().equals(platform.getStatus()), new BusinessException("该平台已禁用"));

        Template template = new Template();
        template.setTemplateName(reqVo.getTemplateName());
        template.setPlatformId(reqVo.getPlatformId());
        template.setMessageUsage(reqVo.getMessageUsage());
        template.setRequirePersist(reqVo.getRequirePersist());
        template.setRequireRepeatConsume(reqVo.getRequireRepeatConsume());
        template.setRequireRateLimit(reqVo.getRequireRateLimit());
        template.setStatus(YesOrNoEnum.NO.getValue());

        if (YesOrNoEnum.YES.getValue().equals(reqVo.getRequireRateLimit())){
            //需要限流 检查限流策略
            ExceptionAssert.throwOnFalse(RateLimitStrategy.getByCode(reqVo.getRateLimitStrategy()) != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "rateLimitStrategy")));
            template.setRateLimitStrategy(reqVo.getRateLimitStrategy());
        }else{
            template.setRateLimitStrategy(null);
        }

        MessageType messageType  = Optional.ofNullable( MessageType.getByCode(reqVo.getMessageType()))
                .orElseThrow(() -> new IllegalRequestException("非法参数；messageType"));
        switch (messageType) {
            case SMS:
                template.setPlatformTemplateId(reqVo.getPlatformTemplateId());
                break;
            case EMAIL:
                template.setContent(reqVo.getContent());
                template.setSubject(reqVo.getSubject());
                template.setRequireHtml(reqVo.getRequireHtml());
                template.setSendAccount(reqVo.getSendAccount());
                template.setUsername(reqVo.getUsername());
                template.setPassword(reqVo.getPassword());
                template.setRequireSsl(reqVo.getRequireSsl());
                break;
            case ROBOT:
                template.setContent(reqVo.getContent());
                break;
//            case MINI_PROGRAM:
//                template.setPage(reqVo.getPage());
//                template.setPlatformTemplateId(reqVo.getPlatformTemplateId());
//                break;
            case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE:
                template.setPlatformTemplateId(reqVo.getPlatformTemplateId());
                break;
            default:
                throw new BusinessException("不支持的消息类型");
        }
        ExceptionAssert.throwOnFalse(!existByTemplateNameAndId(template.getTemplateName(),null), new BusinessException("模板名已存在"));
        this.save(template);
    }

    private boolean existByTemplateNameAndId(String templateName, Long id) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<Template>()
                .eq(Template::getTemplateName, templateName)
                .ne(id != null, Template::getTemplateId, id);
        return this.exists(wrapper);
    }

    @Override
    @Transactional
    public void remove(Long templateId) {
        if (preparedTemplateService.existByTemplateId(templateId)) {
            throw new BusinessException("存在预发送消息模板与该模板绑定，不能删除");
        } else {
            //删除数据，同时同步本地缓存
            DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.TEMPLATE_DELETE, templateId, null);
            Supplier<Boolean> supplier = () -> this.removeById(templateId);
            rocketMQTemplate.sendMessageInTransaction(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build(), supplier);
            //删除模板名缓存
            StatisticsService.deleteTemplateNameCache(templateId);
        }


    }

    @Override
    public void send(MessageSendVo reqVo) throws Exception {

        Template template = this.getOne(new LambdaQueryWrapper<Template>().select(Template::getStatus, Template::getTemplateId,Template::getPlatformId).eq(Template::getTemplateId, reqVo.getTemplateId()));
        ExceptionAssert.throwOnFalse(YesOrNoEnum.YES.getValue().equals(template.getStatus()), new BusinessException("该消息模板已禁用"));
        Platform platform = platformMapper.selectOne(new LambdaQueryWrapper<Platform>().select(Platform::getPlatformId,Platform::getStatus,Platform::getMessageType).eq(Platform::getPlatformId,template.getPlatformId()));
        ExceptionAssert.throwOnFalse(YesOrNoEnum.YES.getValue().equals(platform.getStatus()), new BusinessException("该平台已禁用"));


        MessageType messageType  = Optional.ofNullable( MessageType.getByCode(platform.getMessageType()))
                .orElseThrow(() -> new IllegalRequestException("非法参数；messageType"));
        Set<String> receiveAccounts = new HashSet<>();
        //接受账户校验
        if (messageType != MessageType.ROBOT) {
            //接受账户存在性校验
            ExceptionAssert.throwOnFalse(reqVo.getAccountGroupId() != null || StrUtil.isNotBlank(reqVo.getReceiveAccounts()), new IllegalRequestException("接收账户为空"));
            //输入接受账户校验
            if (StrUtil.isNotBlank(reqVo.getReceiveAccounts())) {
                Set<String> accounts = stringSetOptional(reqVo.getReceiveAccounts());
                //接受账户格式校验
                //格式校验
                boolean result = AccountCheckUtils.checkAccounts(AccountType.getByCode(messageType.getCode()), accounts);
                ExceptionAssert.throwOnFalse(result, new BusinessException("接收账户格式错误"));
                receiveAccounts.addAll(accounts);

            }
            //账户组校验
            if (reqVo.getAccountGroupId() != null) {
                //接受账户组存在性校验
                AccountGroup accountGroup = accountGroupMapper.selectById(reqVo.getAccountGroupId());
                Optional.ofNullable(accountGroup).orElseThrow(() -> new IllegalRequestException("接受账户组不存在"));
                //账户组格式校验
                ExceptionAssert.throwOnFalse(messageType.getCode().equals(accountGroup.getType()), new BusinessException("接受账户组格式与模板不匹配"));
                receiveAccounts.addAll(stringSetOptional(accountGroup.getMembers()));
            }

        }

        SendMessageRequest request = switch (messageType) {
            case SMS -> SendMessageRequest.sendSMSMessageRequestBuilder()
                    .templateId(reqVo.getTemplateId())
                    .contentParams(reqVo.getContentParams())
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(reqVo.getRequireAsync()))
                    .build();
            case EMAIL -> SendMessageRequest.sendEmailMessageRequestBuilder()
                    .templateId(reqVo.getTemplateId())
                    .subjectParams(reqVo.getSubjectParams())
                    .contentParams(reqVo.getContentParams())
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(reqVo.getRequireAsync()))
                    .build();
            case ROBOT -> SendMessageRequest.sendRobotMessageRequestBuilder()
                    .templateId(reqVo.getTemplateId())
                    .contentParams(reqVo.getContentParams())
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(reqVo.getRequireAsync()))
                    .build();
//            case MINI_PROGRAM -> SendMessageRequest.sendMiniProgramMessageRequestBuilder()
//                    .templateId(reqVo.getTemplateId())
//                    .contentParams(reqVo.getContentParams())
//                    .receiveAccounts(receiveAccounts)
//                    .requireAsync(YesOrNoEnum.YES.getValue().equals(reqVo.getRequireAsync()))
//                    .build();
            case WECHAT_OFFICIAL_ACCOUNT_TEMPLATE_MESSAGE -> SendMessageRequest.sendWeChatOfficialAccountMessageBuilder()
                    .templateId(reqVo.getTemplateId())
                    .contentParams(reqVo.getContentParams())
                    .receiveAccounts(receiveAccounts)
                    .requireAsync(YesOrNoEnum.YES.getValue().equals(reqVo.getRequireAsync()))
                    .build();
            default -> throw new BusinessException("不支持的消息类型");
        };
        MessageSendRsp messageSendRsp = messageService.sendMessage(request);
        ExceptionAssert.throwOnFalse(messageSendRsp.getCode() == ResultCodeEnum.SUCCESS.getCode(), new BusinessException(messageSendRsp.getMsg()));
    }

    @Override
    public List<Dic<String>> dic(Integer status) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<Template>().select(Template::getTemplateId, Template::getTemplateName);
        if (status != null){
            wrapper.eq(Template::getStatus, status);
        }
        List<Template> templates = this.list(wrapper);
        return templates.stream().map(template -> new Dic<>(template.getTemplateName(), template.getTemplateId().toString())).collect(Collectors.toList());
    }

    @Override
    public String getTemplateName(Long templateId) {
        Template template = this.getById(templateId);
        return template == null ? null : template.getTemplateName();
    }

    @Override
    public List<OneLayerTreeDic<String, String>> PlatformAndTemplate( Integer status) {

        LambdaQueryWrapper<Platform> platformLambdaQueryWrapper = new LambdaQueryWrapper<Platform>().select(Platform::getPlatformId, Platform::getPlatformName);
        if (status != null){
            platformLambdaQueryWrapper.eq(Platform::getStatus, status);
        }
        List<Platform> platforms = platformMapper.selectList(platformLambdaQueryWrapper);
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<Template>().select(Template::getTemplateId, Template::getTemplateName, Template::getPlatformId);
        if (status != null){
            wrapper.eq(Template::getStatus, status);
        }
        List<Template> templates = this.list(wrapper);
        HashMap<Long, OneLayerTreeDic<String, String>> map = new HashMap<>();
        for (Platform platform : platforms) {
            map.put(platform.getPlatformId(), new OneLayerTreeDic<>(platform.getPlatformName(), platform.getPlatformId().toString(), new ArrayList<>()));
        }
        for (Template template : templates) {
            map.get(template.getPlatformId()).getChildren().add(new Dic<>(template.getTemplateName(), template.getTemplateId().toString()));
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public void status(TemplateStatusEditVo reqVo) throws Exception {
        ExceptionAssert.throwOnFalse(Arrays.stream(YesOrNoEnum.values()).anyMatch(e -> e.getValue().equals(reqVo.getStatus())), new IllegalRequestException("参数值非法：status"));
        LambdaUpdateWrapper<Template> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Template::getStatus, reqVo.getStatus())
                .set(Template::getVersion, reqVo.getVersion() + 1)
                .eq(Template::getTemplateId, reqVo.getTemplateId());
        DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.TEMPLATE_UPDATE, reqVo.getTemplateId(), reqVo.getVersion());
        Supplier<Boolean> supplier = () -> this.update(wrapper);
        rocketMQTemplate.sendMessageInTransaction(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build(), supplier);
    }

    private Set<String> stringSetOptional(String options) {
        return Optional.ofNullable(options)
                .map(s -> s.split(BusinessConstant.LIST_SEPARATOR))
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }


}
