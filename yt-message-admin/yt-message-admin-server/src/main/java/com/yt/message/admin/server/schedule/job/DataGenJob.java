package com.yt.message.admin.server.schedule.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.yt.message.admin.server.service.IPreparedTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @ClassName DadaGenJob
 * @Description 模拟生成数据
 * @Author Ts
 * @Version 1.0
 */
@Component
@Slf4j
public class DataGenJob {
    @Autowired
    private IPreparedTemplateService preparedTemplateService;

    private String[] ids = {"1878866166456418306"
    ,"1878866310669172737","1878866389970878466","1878866450905726977"
    ,"1878866557042589698"};

    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        Random random = new Random();
        String id = ids[random.nextInt(ids.length)];
        preparedTemplateService.sendForTest(Long.parseLong(id));
        log.info("执行成功");
    }







}
