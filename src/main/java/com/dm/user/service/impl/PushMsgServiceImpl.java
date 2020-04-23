package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.PushMsg;
import com.dm.user.mapper.PushMsgMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.PushMsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cui
 */
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
    public void updateByPrimaryKeySelective(PushMsg pm) throws Exception {
        pushMsgMapper.updateByPrimaryKeySelective(pm);
    }

    @Override
    public PageInfo<PushMsg> historyInfo(Integer pageNum) throws Exception {
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<PushMsg> list = pushMsgMapper.historyInfo(LoginUserHelper.getUserId());
        PageInfo<PushMsg> pageInfo = new PageInfo<>(list);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public void readInfo(String pushId) throws Exception {
        pushMsgMapper.updateByRead(pushId);
    }

    @Override
    public void deleteByCertId(String certId) throws Exception {
        pushMsgMapper.deleteByCertId(certId);
    }
}
