package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.Opinion;
import com.dm.user.mapper.OpinionMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OpinionServiceImpl implements OpinionService {

    @Autowired
    private OpinionMapper opinionMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void opinionSubmit(Opinion opinion) throws Exception {
        opinion.setUserId(Integer.parseInt(LoginUserHelper.getUserId()));
        opinion.setPostTime(new Date());
        opinionMapper.insertSelective(opinion);
    }

}
