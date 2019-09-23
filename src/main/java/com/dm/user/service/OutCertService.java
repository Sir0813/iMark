package com.dm.user.service;

import com.dm.user.entity.OutCert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface OutCertService {

    String downloadOutCertTemplate(String certIds) throws Exception;

    void save(OutCert outCert) throws Exception;

    PageInfo<OutCert> list(Page<OutCert> page, String state) throws Exception;

    OutCert details(String outCertId) throws Exception;
}
