package com.dm.user.service.impl;

import com.dm.user.entity.WfItemNode;
import com.dm.user.mapper.WfItemNodeMapper;
import com.dm.user.service.WfItemNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfItemNodeServiceImpl implements WfItemNodeService {

    @Autowired
    private WfItemNodeMapper wfItemNodeMapper;

    @Override
    public WfItemNode selectByItemId(Integer itemid) throws Exception {
        return wfItemNodeMapper.selectByItemId(itemid.toString());
    }

    @Override
    public WfItemNode selectByItemIdDesc(Integer itemid) throws Exception {
        return wfItemNodeMapper.selectByItemIdDesc(itemid.toString());
    }

    @Override
    public WfItemNode selectById(Integer nodeid) throws Exception {
        WfItemNode wfItemNode = wfItemNodeMapper.selectByPrimaryKey(nodeid);
        return wfItemNode;
    }

    @Override
    public WfItemNode selectByOrder(Map<String, Object> map) throws Exception {
        return wfItemNodeMapper.selectByOrder(map);
    }
}
