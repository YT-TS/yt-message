package com.yt.message.admin.server.controller;


import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.vo.*;
import com.yt.message.admin.server.service.IPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
@RestController
@RequestMapping("/platform")
@Slf4j
public class PlatformController {
    @Autowired
    private IPlatformService platformService;

    @PostMapping("/page")
    public PageResult<PlatformRspVo> page(@RequestBody PlatformPageReqVo reqVo) {
        return platformService.page(reqVo);
    }


    @GetMapping("/{platformId}")
    public PlatformRspVo view( @PathVariable("platformId") Long platformId) {
        return platformService.view(platformId);
    }


    @PutMapping
    public void edit(@RequestBody @Validated PlatformEditReqVo reqVo) throws Exception {
        platformService.edit(reqVo);
    }
    @PatchMapping("/status")
    public void status(@RequestBody @Validated PlatformStatusEditVo reqVo) throws Exception {
        platformService.status(reqVo);
    }

    @PostMapping
    public void add(@RequestBody @Validated PlatformReqVo reqVo) throws Exception {
        platformService.add(reqVo);
    }

    @DeleteMapping("/{platformId}")
    public void remove( @PathVariable("platformId") Long platformId) {
        platformService.remove(platformId);
    }

}
