package com.dm.user.service.impl;

import com.dm.user.entity.WfItemNode;
import com.dm.user.mapper.WfItemNodeMapper;
import com.dm.user.service.WfItemNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfItemNodeServiceImpl implements WfItemNodeService {

    @Autowired
    private WfItemNodeMapper wfItemNodeMapper;

    @Override
    public WfItemNode selectByItemId(Integer itemid) throws Exception {
        try {
            return wfItemNodeMapper.selectByItemId(itemid.toString());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
