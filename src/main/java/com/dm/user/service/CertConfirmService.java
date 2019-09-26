package com.dm.user.service;

import com.dm.user.entity.CertConfirm;
import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertConfirmService {

    void deleteByCertId(Integer certId) throws Exception;

    void insertSelective(CertConfirm certConfirm) throws Exception;

    List<CertConfirm> selectByCertId(int parseInt) throws Exception;

    List<CertConfirm> selectByUserId(int parseInt) throws Exception;

    List<CertConfirm> selectByuserIdAndState(String userId, String s) throws Exception;

    void updateByCertId(Map<String, Object> map) throws Exception;

    void updateConfirmState(Map<String, Object> map) throws Exception;

    List<CertConfirm> selectByState(Map<String, Object> map) throws Exception;

    List<CertConfirm> selectByPhone(String username) throws Exception;

    void updateByPrimaryKeySelective(CertConfirm l);
}
