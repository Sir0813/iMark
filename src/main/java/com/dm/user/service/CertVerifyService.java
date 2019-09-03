package com.dm.user.service;

import com.dm.user.entity.CertFicate;

public interface CertVerifyService {

    /**
     * 存证验真
     * @param certFicate
     * @return
     * @throws Exception
     */
    boolean verifyCert(CertFicate certFicate) throws Exception;
}
