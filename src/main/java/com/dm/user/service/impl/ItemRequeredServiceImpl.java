package com.dm.user.service.impl;

import com.dm.user.entity.ItemRequered;
import com.dm.user.mapper.ItemRequeredMapper;
import com.dm.user.service.ItemRequeredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemRequeredServiceImpl implements ItemRequeredService {

    @Autowired
    private ItemRequeredMapper itemRequeredMapper;

    @Override
    public List<ItemRequered> selectByItemId(int itemId) throws Exception {
        List<ItemRequered> list = itemRequeredMapper.selectByItemId(itemId);
        return list;
    }

    @Override
    public List<ItemRequered> selectByRequeredIdAndApplyId(Integer applyid) {
        List<ItemRequered> list = itemRequeredMapper.selectByRequeredIdAndApplyId(applyid);
        return list;
    }

    @Override
    public ItemRequered selectOtherFile(Integer itemid) {
        return itemRequeredMapper.selectOtherFile(itemid);
    }
}
