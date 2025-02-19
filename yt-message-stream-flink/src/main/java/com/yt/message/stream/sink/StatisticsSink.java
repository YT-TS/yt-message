package com.yt.message.stream.sink;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yt.message.common.enums.CacheKey;
import com.yt.message.common.pojo.dto.MessageSendResultPlayLoad;
import com.yt.message.stream.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;

/**
 * @ClassName StatisticsSink
 * @Author Ts
 * @Version 1.0
 */
@Slf4j
public class StatisticsSink implements Sink<MessageSendResultPlayLoad> {


    @SuppressWarnings("deprecation")
    public SinkWriter<MessageSendResultPlayLoad> createWriter(InitContext context) {
        return null;
    }

    @Override
    public SinkWriter<MessageSendResultPlayLoad> createWriter(WriterInitContext context) {
        return new StatisticsSinkWriter();
    }

    private static class StatisticsSinkWriter implements SinkWriter<MessageSendResultPlayLoad> {
        @Override
        public void write(MessageSendResultPlayLoad element, Context context) {
            log.info(element.getMessageId().toString());
            DateTime date = DateUtil.date(element.getTimestamp());
            String dateStr = date.toDateStr();
            //返回一条中第几小时 例如2:15的时间戳 返回 2
            int hour = date.hour(true);
            RedisUtil.pipeline(asyncCommands -> {
                CacheKey cacheKey = CacheKey.YT_STATISTICS_BIZ_AMOUNT;
                //尽量拆分key防止大key和热key的产生 尽量减少命令的数量
                //当天消息每种类型每小时数量 key为每天日期+messageType hashKey为每小时
                asyncCommands.hincrby(cacheKey.getKey(dateStr, "type", element.getMessageType().toString()), hour + "-" + (hour + 1), 1);
                //每小时成功或失败数量  key为每天日期+成功或失败 hashKey为每小时
                asyncCommands.hincrby(cacheKey.getKey(dateStr, "result", element.getResult().toString()), hour + "-" + (hour + 1), 1);

                if (element.getTemplateId() != null) {
                    //当天不同模板请求量
                    asyncCommands.hincrby(cacheKey.getKey(dateStr, "template"), element.getTemplateId().toString(), 1);
                }
                if (element.getPlatformId() != null) {
                    //当天不同平台请求量
                    asyncCommands.hincrby(cacheKey.getKey(dateStr, "platform"), element.getPlatformId().toString(), 1);
                }


            });
        }

        @Override
        public void flush(boolean endOfInput) {

        }

        @Override
        public void close() {

        }
    }
}
