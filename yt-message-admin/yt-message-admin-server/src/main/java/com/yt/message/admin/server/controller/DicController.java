package com.yt.message.admin.server.controller;

import com.yt.message.admin.server.constant.AccountType;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.OneLayerTreeDic;
import com.yt.message.admin.server.service.IAccountGroupService;
import com.yt.message.admin.server.service.IPlatformService;
import com.yt.message.admin.server.service.ITemplateService;
import com.yt.message.common.enums.MessageType;
import com.yt.message.common.enums.MessageUsage;
import com.yt.message.common.enums.RateLimitStrategy;
import com.yt.message.common.enums.YesOrNoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DicController
 * @Description 字典控制器
 * @Author Ts
 * @Version 1.0
 */
@RestController
@RequestMapping("/dic")
public class DicController {
    @Autowired
    private IPlatformService platformService;
    @Autowired
    private ITemplateService templateService;
    @Autowired
    private IAccountGroupService accountGroupService;

    @GetMapping("/messageType")
    public List<Dic<Integer>> messageType(){
        return Arrays.stream(MessageType.values()).map(item->new Dic<>(item.getDesc(), item.getCode())).collect(Collectors.toList());
    }
    @GetMapping("/messageUsage")
    public List<Dic<Integer>> messageUsage(){
        return Arrays.stream(MessageUsage.values()).map(item->new Dic<>(item.getDesc(), item.getKey())).collect(Collectors.toList());
    }
    @GetMapping("/yesOrNo")
    public List<Dic<Integer>> yesOrNo(){
        return Arrays.stream(YesOrNoEnum.values()).map(item->new Dic<>(item.name(), item.getValue())).collect(Collectors.toList());
    }
    @GetMapping({"/template","/template/{status}"})
    public List<Dic<String>> template(@PathVariable(required = false,name = "status") Integer status ){
        return templateService.dic(status);
    }
    @GetMapping({"/platform","/platform/{status}"})
    public List<Dic<String>> platform(@PathVariable(required = false,name = "status") Integer status){
        return platformService.dic(status);
    }
    /*
        platform1
            template1
        platform2
            template2
            template3
    */
    @GetMapping({"/tree/PlatformAndTemplate/{status}","/tree/PlatformAndTemplate"})
    public List<OneLayerTreeDic<String,String>> PlatformAndTemplate(@PathVariable(required = false,name = "status") Integer status){
        return templateService.PlatformAndTemplate(status);
    }
    @GetMapping("/accountGroup")
    public List<Dic<String>> accountGroup(){
        return accountGroupService.dic();
    }
    /*
        type1
            platform1
        type2
            platform2
            platform3
    */
    @GetMapping({"/tree/platform/{status}","/tree/platform"})
    public List<OneLayerTreeDic<Integer,String>> platformTreeDic(@PathVariable(required = false,name = "status") Integer status){
        return platformService.treeDic(status);

    }
    @GetMapping("/accountType")
    public List<Dic<Integer>> accountType(){
        return Arrays.stream(AccountType.values()).map(item->new Dic<>(item.getDesc(), item.getCode())).collect(Collectors.toList());
    }
    @GetMapping("/rateLimitStrategy")
    public List<Dic<Integer>> rateLimitStrategy(){
        return Arrays.stream(RateLimitStrategy.values()).map(item->new Dic<>(item.getDesc(), item.getCode())).collect(Collectors.toList());
    }



}
