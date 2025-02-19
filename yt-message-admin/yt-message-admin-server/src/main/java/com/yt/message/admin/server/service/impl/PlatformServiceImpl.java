package com.yt.message.admin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yt.message.admin.server.exception.BusinessException;
import com.yt.message.admin.server.exception.IllegalRequestException;
import com.yt.message.admin.server.mapper.PlatformMapper;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.OneLayerTreeDic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.pojo.vo.PlatformEditReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformPageReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformReqVo;
import com.yt.message.admin.server.pojo.vo.PlatformRspVo;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.ITemplateService;
import com.yt.message.admin.server.utils.ExceptionAssert;
import com.yt.message.admin.server.utils.PageUtils;
import com.yt.message.common.enums.DataSyncOpeType;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.YesOrNoEnum;
import com.yt.message.common.pojo.dto.DataSyncMessagePayload;
import com.yt.message.mq.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@Service
@Slf4j
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform> implements IPlatformService {

    @Autowired
    private ITemplateService templateService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public PageResult<PlatformRspVo> page(@RequestBody PlatformPageReqVo reqVo) {
        IPage<Platform> page = new Page<>(reqVo.getPageNum(), reqVo.getPageSize());
        LambdaQueryWrapper<Platform> queryWrapper = new LambdaQueryWrapper<Platform>().eq(reqVo.getMessageType() != null, Platform::getMessageType, reqVo.getMessageType());
        this.page(page, queryWrapper);
        return PageUtils.toPageResult(page, PlatformRspVo.class);
    }

    @Override
    public PlatformRspVo view(Long platformId) {
        return BeanUtil.toBean(this.getById(platformId), PlatformRspVo.class);
    }

    @Override
    @Transactional
    public void remove(Long platformId) {
        if (templateService.existByPlatformId(platformId)) {
            throw new BusinessException("存在模板与该平台绑定，不能删除");
        } else {
            //删除数据，同时同步本地缓存
            DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.PLATFORM_DELETE, platformId, null);
            Supplier<Boolean> supplier = () -> this.removeById(platformId);
            rocketMQTemplate.sendMessageInTransaction(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build(), supplier);
            //删除平台名缓存
            StatisticsService.deletePlatformNameCache(platformId);
        }
    }

    @Override
    @Transactional
    public void edit(PlatformEditReqVo reqVo) throws Exception {
        //
        LambdaUpdateWrapper<Platform> updateWrapper = new LambdaUpdateWrapper<Platform>()
                .eq(Platform::getPlatformId, reqVo.getPlatformId())
                .eq(Platform::getMessageType, reqVo.getMessageType())
                .set(Platform::getPlatformName, reqVo.getPlatformName())
                .set(Platform::getHost, reqVo.getHost())
                .set(Platform::getHandlerName, reqVo.getHandlerName())
                .set(Platform::getVersion, reqVo.getVersion() + 1);

        if (StrUtil.isNotEmpty(reqVo.getRateLimitKey())) {


            updateWrapper.set(Platform::getRateLimitKey, reqVo.getRateLimitKey())
                    .set(Platform::getRateLimitScale, reqVo.getRateLimitScale())
                    .set(Platform::getRateLimitInterval, reqVo.getRateLimitInterval());

        } else {
            //清空限流参数
            //检查是否有模板开启了限流
            long count = templateService.count(new LambdaQueryWrapper<Template>()
                    .eq(Template::getPlatformId, reqVo.getPlatformId())
                    .eq(Template::getRequireRateLimit, YesOrNoEnum.YES.getValue())
            );
            ExceptionAssert.throwOnFalse(count <= 0, new BusinessException("存在此平台的模板开启了限流，不能清空限流参数"));
            updateWrapper.set(Platform::getRateLimitKey, null)
                    .set(Platform::getRateLimitScale, null)
                    .set(Platform::getRateLimitInterval, null);

        }
        MessageType messageType = MessageType.getByCode(reqVo.getMessageType());
        ExceptionAssert.throwOnFalse(messageType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageType")));
        switch (messageType) {
            case SMS:
                updateWrapper.set(Platform::getAppId, reqVo.getAppId())
                        .set(Platform::getSecretId, reqVo.getSecretId())
                        .set(Platform::getSecretKey, reqVo.getSecretKey())
                        .set(Platform::getSignName, reqVo.getSignName())
                        .set(Platform::getPort, reqVo.getPort());
                break;
            case EMAIL:
                updateWrapper.set(Platform::getPort, reqVo.getPort());
                break;
            case ROBOT:
                break;
        }

        ExceptionAssert.throwOnFalse(!this.existByNameAndId(reqVo.getPlatformName(),reqVo.getPlatformId()), new BusinessException("平台名已存在"));
        //更新数据库 同时同步本地缓存
        DataSyncMessagePayload payload = new DataSyncMessagePayload(DataSyncOpeType.PLATFORM_UPDATE, reqVo.getPlatformId(), reqVo.getVersion());
        Supplier<Boolean> supplier = () -> this.update(updateWrapper);
        rocketMQTemplate.sendMessageInTransaction(MQConstant.DATA_SYNC_TOPIC_NAME, MessageBuilder.withPayload(payload).build(), supplier);
        //删除平台名缓存
        StatisticsService.deletePlatformNameCache(reqVo.getPlatformId());
    }

    @Override
    public void add(PlatformReqVo reqVo) throws Exception {
        MessageType messageType = MessageType.getByCode(reqVo.getMessageType());
        ExceptionAssert.throwOnFalse(messageType != null, new IllegalRequestException(StrUtil.format("非法参数，{}", "messageType")));
        Platform platform = new Platform();
        platform.setPlatformName(reqVo.getPlatformName());
        platform.setMessageType(reqVo.getMessageType());
        platform.setHost(reqVo.getHost());
        platform.setHandlerName(reqVo.getHandlerName());
        //限流配置
        if (StrUtil.isNotEmpty(reqVo.getRateLimitKey())) {
            platform.setRateLimitKey(reqVo.getRateLimitKey());
            platform.setRateLimitScale(reqVo.getRateLimitScale());
            platform.setRateLimitInterval(reqVo.getRateLimitInterval());


        }

        switch (messageType) {
            case SMS:
                platform.setAppId(reqVo.getAppId());
                platform.setSecretId(reqVo.getSecretId());
                platform.setSecretKey(reqVo.getSecretKey());
                platform.setSignName(reqVo.getSignName());
                platform.setPort(reqVo.getPort());
                break;
            case EMAIL:
                platform.setPort(reqVo.getPort());
                break;
            case ROBOT:
                break;
        }
        ExceptionAssert.throwOnFalse(!this.existByNameAndId(platform.getPlatformName(),null), new BusinessException("平台名已存在"));
        this.save(platform);
    }

    private boolean existByNameAndId(String name, Long id) {
        LambdaQueryWrapper<Platform> wrapper = new LambdaQueryWrapper<Platform>()
                .eq(Platform::getPlatformName, name)
                .ne(id != null, Platform::getPlatformId, id);
        return this.exists(wrapper);
    }

    @Override
    public List<OneLayerTreeDic<Integer, String>> treeDic() {
        LambdaQueryWrapper<Platform> queryWrapper = new LambdaQueryWrapper<Platform>()
                .select(Platform::getPlatformId, Platform::getPlatformName, Platform::getMessageType);
        List<Platform> platforms = this.list(queryWrapper);

        Map<Integer, List<Dic<String>>> typeMap = new HashMap<>();
        for (Platform platform : platforms) {
            List<Dic<String>> dicList = typeMap.computeIfAbsent(platform.getMessageType(), k -> new ArrayList<>());
            dicList.add(new Dic<>(platform.getPlatformName(), platform.getPlatformId().toString()));
        }

        return CollUtil.isEmpty(typeMap) ? Collections.emptyList() : typeMap.keySet().stream().map(
                        key ->
                                new OneLayerTreeDic<Integer, String>(MessageType.getByCode(key).getDesc(), key, null)
                                        .setChildren(typeMap.get(key))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Dic<String>> dic() {
        return this.list().stream().map(platform -> new Dic<>(platform.getPlatformName(), platform.getPlatformId().toString())).collect(Collectors.toList());

    }

    @Override
    public String getPlatformName(long l) {
        Platform platform = this.getById(l);
        return platform == null ? null : platform.getPlatformName();
    }


}
