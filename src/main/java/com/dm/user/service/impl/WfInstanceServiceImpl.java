package com.dm.user.service.impl;

import com.dm.user.entity.WfInstance;
import com.dm.user.mapper.WfInstanceMapper;
import com.dm.user.service.WfInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfInstanceServiceImpl implements WfInstanceService {

    @Autowired
    private WfInstanceMapper wfInstanceMapper;

    @Override
    public void insert(WfInstance wfInstance) throws Exception {
        wfInstanceMapper.insertSelective(wfInstance);
    }

    @Override
    public WfInstance selectById(String wfInstanceId) throws Exception {
        return wfInstanceMapper.selectByPrimaryKey(Integer.parseInt(wfInstanceId));
    }

    @Override
    public void updateById(WfInstance wfInstance) throws Exception {
        wfInstanceMapper.updateByPrimaryKeySelective(wfInstance);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> map) throws Exception {
        wfInstanceMapper.updateByInstanceId(map);
    }
}
