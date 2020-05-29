package com.dm.user.service.impl;

import com.dm.user.entity.BizCertModel;
import com.dm.user.mapper.BizCertModelMapper;
import com.dm.user.service.BizCertService;
import com.dm.user.service.OrgItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class BizCertServiceImpl implements BizCertService {

    @Autowired
    private BizCertModelMapper bizCertModelMapper;

    @Autowired
    private OrgItemService orgItemService;

    @Override
    public BizCertModel selectByItemId(Integer itemid) {
        BizCertModel bizCertModel = bizCertModelMapper.selectByItemId(itemid);
        return bizCertModel;
    }

    @Override
    public Map<String, Object> certInfo(Integer applyId) {
        return bizCertModelMapper.selectInfoByApplyId(applyId);
    }
}
