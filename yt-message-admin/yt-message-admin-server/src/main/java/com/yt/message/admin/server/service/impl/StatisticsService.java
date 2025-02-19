package com.yt.message.admin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.IStatisticsService;
import com.yt.message.admin.server.service.ITemplateService;
import com.yt.message.common.enums.CacheKey;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName StatisticsService
 * @Author Ts
 * @Version 1.0
 */
@Service
public class StatisticsService implements IStatisticsService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplateForLong;
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private IPlatformService platformService;
    private static final List<String> hourKeys ;


    private static  Map<String, String> templateNameCache  = new HashMap<>() ;

    private static  Map<String, String> platformNameCache = new HashMap<>() ;





    static {
        hourKeys = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String hourKey = i + "-" + (i + 1);
            hourKeys.add(hourKey);
        }
    }

   public static void deleteTemplateNameCache(Long templateId){
        templateNameCache.remove(templateId.toString());
   }

    public static void deletePlatformNameCache(Long platformId){
        platformNameCache.remove(platformId.toString());
    }

    @Override
    public Map<String, Object> hour() {
        HashOperations<String, String, Long> hashOperations = redisTemplateForLong.opsForHash();
        String dateStr = DateUtil.date().toDateStr();

        Map<String, Long> successResultOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "result", ResultCodeEnum.SUCCESS.getCode()));
        Map<String, Long> failResultOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "result", ResultCodeEnum.FAIL.getCode()));
        List<Long> allOfHour = new ArrayList<>(32);
        for (String hourKey : hourKeys) {
            Long successNum = successResultOfHour.getOrDefault(hourKey, 0L);
            Long failNum = failResultOfHour.getOrDefault(hourKey, 0L);
            //按小时排好顺序
            allOfHour.add(successNum + failNum);
        }
        Map<String, List<Long>> typesOfHour = new HashMap<>();
        for (MessageType value : MessageType.values()) {
            //key 为 hourKey
            Map<String, Long> hourMap = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "type", value.getCode().toString()));
            if (CollUtil.isEmpty(hourMap)){
                continue;
            }
            List<Long>  hourNumList = new ArrayList<>(32);
            for (String hourKey : hourKeys) {
                //
                hourNumList.add( hourMap.getOrDefault(hourKey, 0L));
            }
            typesOfHour.put(value.getDesc(),hourNumList);
        }
        return new HashMap<>(){{
            put("pie", allOfHour);
            put("bar", typesOfHour);
        }};
    }

    @Override
    public Map<String, Object> today() {
        HashOperations<String, String, Long> hashOperations = redisTemplateForLong.opsForHash();
        String dateStr = DateUtil.date().toDateStr();

        //当天消息类型请求量
        Map<String, Long> typesOfRequest = new HashMap<>();
        for (MessageType value : MessageType.values()) {
            //key 为 hourKey
            Map<String, Long> hourMap = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "type", value.getCode().toString()));
            if (CollUtil.isEmpty(hourMap)){
                continue;
            }
            Long sum = 0L;
            for (String hourKey : hourKeys) {
                //
                sum += hourMap.getOrDefault(hourKey, 0L);

            }
            typesOfRequest.put(value.getDesc(),sum);
        }
        //当天消息模板请求量
        Map<String, Long> templatesIdMap = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "template"));
        Map<String, Long> templatesNameMap = templatesIdMap.entrySet().stream().collect(Collectors.toMap(
                entry -> {
                    String templateName = templateNameCache.get(entry.getKey());
                    if (templateName == null) {
                        templateName = templateService.getTemplateName(Long.parseLong(entry.getKey()));
                        if (templateName != null){
                            templateNameCache.put(entry.getKey(), templateName);
                        }
                    }
                    return templateName == null ? entry.getKey() : templateName;
                },
                Map.Entry::getValue
        ));

        //当天消息平台请求量
        Map<String, Long> platformIdMap = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "platform"));
        Map<String, Long> platformNameMap =  platformIdMap.entrySet().stream().collect(Collectors.toMap(
                entry -> {
                    String platformName = platformNameCache.get(entry.getKey());
                    if (platformName == null) {
                        platformName = platformService.getPlatformName(Long.parseLong(entry.getKey()));
                        if (platformName != null){
                            platformNameCache.put(entry.getKey(), platformName);
                        }
                    }
                    return platformName == null ? entry.getKey() : platformName;
                },
                Map.Entry::getValue
        ));


        return new HashMap<>(){{
            put("typesOfRequest", typesOfRequest);
            put("templatesOfRequest", templatesNameMap);
            put("platformsOfRequest", platformNameMap);
        }};
    }



    @Override
    public Map<String, Object> total() {
        HashOperations<String, String, Long> hashOperations = redisTemplateForLong.opsForHash();

        DateTime today = DateUtil.date();
        String dateStr = today.toDateStr();

        //当天每小时成功请求量
        Map<String, Long> successOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "result", ResultCodeEnum.SUCCESS.getCode()));
        //当天每小时失败请求量
        Map<String, Long> failOfHour = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey(dateStr, "result", ResultCodeEnum.FAIL.getCode()));
        //当天成功请求量
        Long todaySuccessTotal = successOfHour.values().stream().mapToLong(Long::longValue).sum();
        //当天失败请求量
        Long todayFailTotal = failOfHour.values().stream().mapToLong(Long::longValue).sum();
        //当天总请求量
        Long todayTotal = todaySuccessTotal + todayFailTotal;

        //本月成功请求量
        //先从缓存中拿到今天之前的
        int month = today.monthBaseOne();
        Map<String, Long> monthOfRequest = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey("month",month));
        Long monSuccessTotal = todaySuccessTotal + monthOfRequest.getOrDefault(String.valueOf(ResultCodeEnum.SUCCESS.getCode()),0L);
        //本月失败请求量
        Long monFailTotal = todayFailTotal + monthOfRequest.getOrDefault(String.valueOf(ResultCodeEnum.FAIL.getCode()),0L);
        //本月总请求量
        Long monTotal = monSuccessTotal + monFailTotal;

        //历史成功请求量
        Map<String, Long> historyOfRequest  = hashOperations.entries(CacheKey.YT_STATISTICS_BIZ_AMOUNT.getKey("history"));
        Long historySuccessTotal = todaySuccessTotal + historyOfRequest.getOrDefault(String.valueOf(ResultCodeEnum.SUCCESS.getCode()),0L);
        //历史失败请求量
        Long historyFailTotal = todayFailTotal + historyOfRequest.getOrDefault(String.valueOf(ResultCodeEnum.FAIL.getCode()),0L);
        //历史总请求量
        Long historyTotal = historySuccessTotal + historyFailTotal;




        return new HashMap<>(){{
            put("todayTotal", todayTotal);
            put("todaySuccessTotal", todaySuccessTotal);
            put("todayFailTotal", todayFailTotal);
            put("monthTotal", monTotal);
            put("monthSuccessTotal", monSuccessTotal);
            put("monthFailTotal", monFailTotal);
            put("historyTotal", historyTotal);
            put("historySuccessTotal", historySuccessTotal);
            put("historyFailTotal", historyFailTotal);
        }};
    }


}
