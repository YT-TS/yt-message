package com.yt.message.admin.client.service;

import com.yt.message.admin.client.pojo.dto.PlatformCache;
import com.yt.message.admin.client.pojo.dto.TemplateCache;

/**
 * @ClassName CacheSyncService
 * @Author Ts
 * @Version 1.0
 */

public interface CacheSyncService {

    PlatformCache syncPlatform(Long platformId);
    TemplateCache syncTemplate(Long templateId);



}
