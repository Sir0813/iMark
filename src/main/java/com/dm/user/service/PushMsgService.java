package com.dm.user.service;

import com.dm.user.entity.PushMsg;

import java.util.List;

public interface PushMsgService {

    void insertSelective(PushMsg pm);

    List<PushMsg> selectByReceiveAndState(String username);

    void updateByPrimaryKeySelective(PushMsg pm);
}
