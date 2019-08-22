package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.CertTemplate;

public interface CertTemplateService {

    Result list() throws Exception;

    CertTemplate getByTemplateType(String type) throws Exception;

    String fileEdit(String certId) throws Exception;
}
