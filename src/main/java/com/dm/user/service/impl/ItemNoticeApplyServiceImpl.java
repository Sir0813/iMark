package com.dm.user.service.impl;

import com.dm.user.entity.ItemNoticeApply;
import com.dm.user.mapper.ItemNoticeApplyMapper;
import com.dm.user.service.ItemNoticeApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemNoticeApplyServiceImpl implements ItemNoticeApplyService {

    @Autowired
    private ItemNoticeApplyMapper noticeApplyMapper;

    @Override
    public void insertData(ItemNoticeApply itemNoticeApply) {
        noticeApplyMapper.insertSelective(itemNoticeApply);
    }
}
