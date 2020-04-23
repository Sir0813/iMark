package com.dm.user.service.impl;

import com.dm.user.entity.Org;
import com.dm.user.mapper.OrgMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.OrgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgMapper orgMapper;

    @Override
    public PageInfo<Org> orgList(Integer pageNum) throws Exception {
        pageNum = null == pageNum ? 1 : pageNum;
        PageHelper.startPage(pageNum, StateMsg.PAGE_SIZE);
        List<Org> orgList = orgMapper.orgList();
        PageInfo<Org> pageInfo = new PageInfo<>(orgList);
        if (pageNum > pageInfo.getPages()) {
            return null;
        }
        return pageInfo;
    }

    @Override
    public String notice(int orgId) throws Exception {
        Org org = orgMapper.selectByPrimaryKey(orgId);
        return org.getServeDesc();
    }

    @Override
    public Org selectById(Integer orgid) throws Exception {
        return orgMapper.selectByPrimaryKey(orgid);
    }
}
