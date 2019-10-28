package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.CertTemplate;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertTemplateService {

    /**
     * 存证模板列表
     *
     * @return
     * @throws Exception
     */
    Result list() throws Exception;

    /**
     * 根据类型获取模板
     *
     * @param type
     * @return
     * @throws Exception
     */
    CertTemplate getByTemplateType(String type) throws Exception;

    /**
     * 模板编辑
     *
     * @param certId
     * @return
     * @throws Exception
     */
    String fileEdit(String certId) throws Exception;
}
