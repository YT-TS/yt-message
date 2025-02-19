package com.yt.message.admin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yt.message.admin.server.pojo.dto.PageResult;
import com.yt.message.admin.server.pojo.entity.PreparedTemplate;
import com.yt.message.admin.server.pojo.vo.PreparedTemplatePageReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateReqVo;
import com.yt.message.admin.server.pojo.vo.PreparedTemplateRspVo;
import com.yt.message.admin.server.pojo.vo.validation.PreparedTemplateEditReqVo;

/**
 * <p>
 * 预发送的消息模板，可直接用来发送消息 服务类
 * </p>
 *
 * @author yt
 * @since 2025-01-09
 */
public interface IPreparedTemplateService extends IService<PreparedTemplate> {

    PageResult<PreparedTemplateRspVo> page(PreparedTemplatePageReqVo reqVo);
    boolean existByTemplateId(Long TemplateId);

    boolean existByAccountGroupId(Long accountGroupId);

    void add(PreparedTemplateReqVo reqVo) throws Exception;

    void remove(Long templateId);

    void edit(PreparedTemplateEditReqVo reqVo) throws Exception;

    void send(Long preparedTemplateId) throws Exception;
    void sendForTest(Long preparedTemplateId) throws Exception;
}
