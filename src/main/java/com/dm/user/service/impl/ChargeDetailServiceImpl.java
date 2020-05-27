package com.dm.user.service.impl;

import com.dm.user.entity.ApplyFee;
import com.dm.user.entity.ChargeDetail;
import com.dm.user.mapper.ChargeDetailMapper;
import com.dm.user.service.ChargeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChargeDetailServiceImpl implements ChargeDetailService {

    @Autowired
    private ChargeDetailMapper chargeDetailMapper;

    @Override
    public void deleteByApplyId(Integer applyid) {
        chargeDetailMapper.updateIsDel(applyid);
    }

    @Override
    public void insertList(List<ChargeDetail> chargeDetails) {
        chargeDetailMapper.insertList(chargeDetails);
    }

    @Override
    public void insertData(ChargeDetail preCharge) {
        chargeDetailMapper.insertSelective(preCharge);
    }

    @Override
    public ChargeDetail selectByChargeIdAndApplyId(Map<String, Object> map) {
        return chargeDetailMapper.selectByChargeIdAndApplyId(map);
    }

    @Override
    public ChargeDetail selectById(Integer id) {
        return chargeDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ApplyFee> selectByApplyId(int applyId) {
        return chargeDetailMapper.selectByApplyId(applyId);
    }

    @Override
    public ApplyFee selectByApplyIdAndFileNum(Integer applyId) {
        return chargeDetailMapper.selectByApplyIdAndFileNum(applyId);
    }

    @Override
    public void updateByIdAndApplyId(Map<String, Object> chargeMap) {
        chargeDetailMapper.updateByIdAndApplyId(chargeMap);
    }

    @Override
    public ApplyFee selectByApplyIdAndStatus(int applyid) {
        return chargeDetailMapper.selectByApplyIdAndStatus(applyid);
    }

    @Override
    public void updatePayStatus(Integer id) {
        chargeDetailMapper.updatePayStatus(id);
    }
}
