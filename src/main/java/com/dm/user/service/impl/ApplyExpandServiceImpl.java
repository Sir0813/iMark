package com.dm.user.service.impl;

import com.dm.user.entity.ApplyExpand;
import com.dm.user.mapper.ApplyExpandMapper;
import com.dm.user.service.ApplyExpandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApplyExpandServiceImpl implements ApplyExpandService {

    @Autowired
    private ApplyExpandMapper applyExpandMapper;

    @Override
    public void insert(ApplyExpand applyExpand) {
        applyExpandMapper.insertSelective(applyExpand);
    }

    @Override
    public void update(ApplyExpand applyExpand) {
        applyExpandMapper.updateByPrimaryKeySelective(applyExpand);
    }

    @Override
    public ApplyExpand selectByApplyId(int applyid) {
        return applyExpandMapper.selectByApplyId(applyid);
    }
}
