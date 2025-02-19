package com.yt.message.admin.server.schedule.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yt.message.admin.server.pojo.entity.DailyStatistics;
import com.yt.message.admin.server.service.IDailyStatisticsService;
import com.yt.message.common.enums.CacheKey;
import com.yt.message.common.enums.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName StatisticsJob
 * @Author Ts
 * @Version 1.0
 */
@Component
public class StatisticsJob {
    @Autowired
    private RedisTemplate<String,Object> redisTemplateForLong;
    @Autowired
    private IDailyStatisticsService dailyStatisticsService;


    @XxlJob("statisticsJobHandler")
    public void statisticsJobHandler() {
        HashOperations<String, String, Long> hashOperations = redisTemplateForLong.opsForHash();


        DateTime yesterday = DateUtil.yesterday();
        String yesterdayDateStr = yesterday.toDateStr();
        int yesterdayMonth = yesterday.monthBaseOne();


        //昨天每小时成功请求量
        Map<String, Long> successOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(yesterdayDateStr, "result", ResultCodeEnum.SUCCESS.getCode()));
        //昨天每小时失败请求量
        Map<String, Long> failOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(yesterdayDateStr, "result", ResultCodeEnum.FAIL.getCode()));
        //昨天成功请求量
        long successTodayTotal = successOfHour.values().stream().mapToLong(Long::longValue).sum();
        //昨天失败请求量
        long failTodayTotal = failOfHour.values().stream().mapToLong(Long::longValue).sum();
        //昨天总请求量
        long todayTotal = successTodayTotal + failTodayTotal;


        //先写入数据库
        DailyStatistics dailyStatistics = new DailyStatistics();
        dailyStatistics.setDate(yesterday);
        dailyStatistics.setNumberOfFail(failTodayTotal);
        dailyStatistics.setNumberOfSuccess(successTodayTotal);
        dailyStatisticsService.save(dailyStatistics);


        //更新缓存
        //更新月成功请求量
        hashOperations.increment(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey( "month",yesterdayMonth), String.valueOf(ResultCodeEnum.SUCCESS.getCode()), successTodayTotal);
        //更新月失败请求量
        hashOperations.increment(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey( "month",yesterdayMonth), String.valueOf(ResultCodeEnum.FAIL.getCode()), failTodayTotal);
        //更新历史成功请求量
        hashOperations.increment(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey( "history"), String.valueOf(ResultCodeEnum.SUCCESS.getCode()), successTodayTotal);
        //更新历史失败请求量
        hashOperations.increment(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey( "history"), String.valueOf(ResultCodeEnum.FAIL.getCode()), failTodayTotal);







    }
}
