package com.yt.message.admin.server.service;

import java.util.Map;

/**
 * @ClassName IStatisticsService
 * @Author Ts
 * @Version 1.0
 */

public interface IStatisticsService  {


    Map<String,Object> hour();

    Map<String, Object> today();

    Map<String, Object> total();


}
