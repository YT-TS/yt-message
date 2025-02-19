package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName StatisticsController
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("/hour")
    public Map<String, Object> hour() {
        return statisticsService.hour();
    }

    @GetMapping("/today")
    public Map<String, Object> today() {
        return statisticsService.today();
    }

    @GetMapping("/total")
    public Map<String, Object> total() {
        return statisticsService.total();
    }


}
