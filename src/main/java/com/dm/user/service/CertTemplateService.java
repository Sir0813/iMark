package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.CertTemplate;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertTemplateService {

    Result list() throws Exception;

    CertTemplate getByTemplateType(String type) throws Exception;

    String fileEdit(String certId) throws Exception;
}
