package com.dm.user.service;

import com.dm.user.entity.PushMsg;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PushMsgService {

    void insertSelective(PushMsg pm) throws Exception;

    List<PushMsg> selectByReceiveAndState(String username) throws Exception;

    void updateByPrimaryKeySelective(PushMsg pm) throws Exception;

    PageInfo<PushMsg> historyInfo(Page<PushMsg> page) throws Exception;

    void readInfo(String pushId) throws Exception;
}
