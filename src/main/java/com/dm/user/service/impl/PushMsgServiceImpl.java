package com.dm.user.service.impl;

import com.dm.user.entity.PushMsg;
import com.dm.user.mapper.PushMsgMapper;
import com.dm.user.service.PushMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PushMsgServiceImpl implements PushMsgService {

    @Autowired
    private PushMsgMapper pushMsgMapper;

    @Override
    public void insertSelective(PushMsg pm) {
        pushMsgMapper.insertSelective(pm);
    }

    @Override
    public List<PushMsg> selectByReceiveAndState(String username) {
        List<PushMsg> pmList = pushMsgMapper.selectByReceiveAndState(username);
        return pmList;
    }

    @Override
    public void updateByPrimaryKeySelective(PushMsg pm) {
        pushMsgMapper.updateByPrimaryKeySelective(pm);
    }
}
