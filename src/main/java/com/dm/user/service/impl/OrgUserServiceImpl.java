package com.dm.user.service.impl;

import com.dm.user.entity.OrgUser;
import com.dm.user.mapper.OrgUserMapper;
import com.dm.user.service.OrgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgUserServiceImpl implements OrgUserService {

    @Autowired
    private OrgUserMapper orgUserMapper;

    @Override
    public OrgUser selectByUserId(int userId) {
        return orgUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<OrgUser> selectAdminList() {
        return orgUserMapper.selectAdminList();
    }
}
