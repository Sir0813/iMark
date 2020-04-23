package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizItemAgreementWithBLOBs;
import com.dm.user.mapper.BizItemAgreementMapper;
import com.dm.user.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AgreementServiceImpl implements AgreementService {

    @Autowired
    private BizItemAgreementMapper agreementMapper;

    @Override
    public Result desc() throws Exception {
        BizItemAgreementWithBLOBs agreementWithBLOBs = agreementMapper.selectByOrgId(11);
        return ResultUtil.success(agreementWithBLOBs);
    }
}
