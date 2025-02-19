package com.yt.message.admin.server.mq.listener;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yt.message.admin.server.pojo.entity.Platform;
import com.yt.message.admin.server.pojo.entity.Template;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.ITemplateService;
import com.yt.message.common.pojo.dto.DataSyncMessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.function.Supplier;

/**
 * @ClassName DataSync
 * @Description 数据同步消息的本地事务监听器
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
@RocketMQTransactionListener
public class DataSyncLocalTransactionListener implements RocketMQLocalTransactionListener {
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private IPlatformService platformService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        Boolean result = null;
        if (arg instanceof Supplier<?>) {
            Supplier<?> supplier = (Supplier<?>) arg;
            try {
                Object o = supplier.get();
                if (o instanceof Boolean) {
                    result = (Boolean) o;
                }
            } catch (Exception e) {
                log.error("数据同步消息本地事务执行失败", e);
                return RocketMQLocalTransactionState.UNKNOWN;
            }
        }
        if (result == null) {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        if (result) {
            log.info("提交事务消息");
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            log.info("回滚事务消息");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //其实可以直接返回提交 不管修改是否成功 我都删除本地缓存 重新同步
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        DataSyncMessagePayload payload = JSONUtil.toBean(new String((byte[]) msg.getPayload()), DataSyncMessagePayload.class);
        long count;
        switch (payload.getType()) {
            case PLATFORM_DELETE:
                //检查是否存在 来判断是否删除成功
                count = platformService.count(new LambdaQueryWrapper<Platform>().eq(Platform::getPlatformId, payload.getId()));
                return count == 0 ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
            case PLATFORM_UPDATE:
                //检查版本号 来判断是否更新成功
                Platform platform = platformService.getOne(new LambdaQueryWrapper<Platform>().select(Platform::getVersion).eq(Platform::getPlatformId, payload.getId()));
                return !platform.getVersion().equals(payload.getVersion()) ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
            case TEMPLATE_DELETE:
                log.info("删除模板");
                count = templateService.count(new LambdaQueryWrapper<Template>().eq(Template::getTemplateId, payload.getId()));
                return count == 0 ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
            case TEMPLATE_UPDATE:
                Template template = templateService.getOne(new LambdaQueryWrapper<Template>().select(Template::getVersion).eq(Template::getTemplateId, payload.getId()));
                return !template.getVersion().equals(payload.getVersion()) ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }


}
