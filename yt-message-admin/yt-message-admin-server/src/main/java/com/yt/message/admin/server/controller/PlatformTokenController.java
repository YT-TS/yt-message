package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.service.impl.PlatformTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PlatformTokenController
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform/token")
public class PlatformTokenController {


    @Autowired
    private PlatformTokenService platformTokenService;
    @GetMapping
    public List<Map<String,Object>> list() {
        return platformTokenService.list();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id) throws Exception {
        platformTokenService.update(id);
    }

}
