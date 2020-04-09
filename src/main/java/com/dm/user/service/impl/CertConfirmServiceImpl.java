package com.dm.user.service.impl;

import com.dm.user.entity.CertConfirm;
import com.dm.user.mapper.CertConfirmMapper;
import com.dm.user.msg.ConfirmEnum;
import com.dm.user.service.CertConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CertConfirmServiceImpl implements CertConfirmService {

    @Autowired
    private CertConfirmMapper certConfirmMapper;

    @Override
    public void deleteByCertId(Integer certId) throws Exception {
        certConfirmMapper.deleteByCertId(certId);
    }

    @Override
    public void insertSelective(CertConfirm certConfirm) throws Exception {
        certConfirmMapper.insertSelective(certConfirm);
    }

    @Override
    public List<CertConfirm> selectByCertId(int parseInt) throws Exception {
        return certConfirmMapper.selectByCertId(parseInt);
    }

    @Override
    public List<CertConfirm> selectByUserId(int parseInt) throws Exception {
        return certConfirmMapper.selectByUserId(parseInt);
    }

    @Override
    public List<CertConfirm> selectByuserIdAndState(String userId, String s) throws Exception {
        return certConfirmMapper.selectByuserIdAndState(userId, s);
    }

    @Override
    public void updateByCertId(Map<String, Object> map) throws Exception {
        map.put("returnConfirm", ConfirmEnum.RETURN_CONFIRM.getCode());
        map.put("originator", ConfirmEnum.ORIGINATOR.getCode());
        certConfirmMapper.updateByCertId(map);
    }

    @Override
    public void updateConfirmState(Map<String, Object> map) throws Exception {
        map.put("isConfirm", ConfirmEnum.IS_CONFIRM.getCode());
        certConfirmMapper.updateConfirmState(map);
    }

    @Override
    public List<CertConfirm> selectByState(Map<String, Object> map) throws Exception {
        map.put("noConfirm", ConfirmEnum.NO_CONFIRM.getCode());
        return certConfirmMapper.selectByState(map);
    }

    @Override
    public List<CertConfirm> selectByPhone(String username) throws Exception {
        return certConfirmMapper.selectByPhone(username);
    }

    @Override
    public void updateByPrimaryKeySelective(CertConfirm l) {
        certConfirmMapper.updateByPrimaryKeySelective(l);
    }
}
