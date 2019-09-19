package com.dm.user.service;

import com.dm.user.entity.OutCert;

public interface OutCertService {

    String downloadOutCertTemplate(String certIds) throws Exception;

    void save(OutCert outCert) throws Exception;
}
