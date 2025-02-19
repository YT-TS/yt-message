package com.yt.message.admin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;
import com.yt.message.admin.client.service.CacheSyncService;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CacheSyncServiceImpl
 * @Author Ts
 * @Version 1.0
 */
@Service
//@DubboService(timeout = 3000)
@DubboService
@Slf4j
public class CacheSyncServiceImpl implements CacheSyncService {


    @Autowired
    private IPlatformService platformService;
    @Autowired
    private ITemplateService templateService;

    @Override
    public PlatformCache syncPlatform(Long platformId) {
        Platform platform = platformService.getById(platformId);
        log.info("同步信息{}", platformId);
        if (platform == null) {
            return null;
        }
        return BeanUtil.toBean(platform, PlatformCache.class);
    }

    @Override
    public TemplateCache syncTemplate(Long templateId) {
        Template template = templateService.getById(templateId);
        if (template == null) {
            return null;
        }
        return BeanUtil.toBean(template, TemplateCache.class);

    }
}
