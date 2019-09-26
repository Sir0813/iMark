package com.dm.user.service;

import com.dm.user.entity.CertFicate;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertVerifyService {

    boolean verifyCert(CertFicate certFicate) throws Exception;
}
