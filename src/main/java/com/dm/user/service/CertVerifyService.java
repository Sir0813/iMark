package com.dm.user.service;

import com.dm.user.entity.CertFicate;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertVerifyService {

    /**
     * 存证校验
     * @param certFicate
     * @return
     * @throws Exception
     */
    boolean verifyCert(CertFicate certFicate) throws Exception;
}
