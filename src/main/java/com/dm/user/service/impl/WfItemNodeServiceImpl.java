package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfItemNode;
import com.dm.user.mapper.WfItemNodeMapper;
import com.dm.user.service.WfItemNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void deleteByUserId(String userId, int applyId) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", userId);
        map.put("applyId", applyId);
        wfItemNodeMapper.deleteByUserId(map);
    }

    @Override
    public void insertData(WfItemNode wfItemNode) {
        wfItemNodeMapper.insertData(wfItemNode);
    }

    @Override
    public List<ProgressView> isNotProgress(Integer applyid) {
        return wfItemNodeMapper.isNotProgress(applyid);
    }
}
