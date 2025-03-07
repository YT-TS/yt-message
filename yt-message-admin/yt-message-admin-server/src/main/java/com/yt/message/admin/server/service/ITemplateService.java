package com.yt.message.admin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.message.admin.server.pojo.dto.Dic;
import com.yt.message.admin.server.pojo.dto.OneLayerTreeDic;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.Template;

import com.yt.message.admin.server.pojo.vo.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 消息模板 服务类
 * </p>
 *
 * @author yt
 * @since 2024-11-26
 */
public interface ITemplateService extends IService<Template> {


    boolean existByPlatformId(Long platformId);

    PageResult<TemplateRspVo> page(TemplatePageReqVo reqVo);

    void edit(TemplateEditReqVo reqVo) throws Exception;

    void add(TemplateReqVo reqVo) throws Exception;

    void remove(Long templateId);

    void send(MessageSendVo reqVo) throws Exception;

    List<Dic<String>> dic(Integer status);

    String getTemplateName(Long templateId);


    List<OneLayerTreeDic<String,String>> PlatformAndTemplate(@PathVariable(required = false) Integer status);

    void status(TemplateStatusEditVo reqVo);
}
