package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.WfItemNode;
import com.dm.user.mapper.WfItemNodeMapper;
import com.dm.user.service.WfItemNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfItemNodeServiceImpl implements WfItemNodeService {

    @Autowired
    private WfItemNodeMapper wfItemNodeMapper;

    @Override
    public WfItemNode selectById(Integer nodeid) throws Exception {
        WfItemNode wfItemNode = wfItemNodeMapper.selectByPrimaryKey(nodeid);
        return wfItemNode;
    }

    @Override
    public List<ProcessConfig> selectByUserId() throws Exception {
        return wfItemNodeMapper.selectByUserId(LoginUserHelper.getUserId());
    }
}
