package com.dm.user.service.impl;

import com.dm.user.entity.ItemCharge;
import com.dm.user.mapper.ItemChargeMapper;
import com.dm.user.service.ItemChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemChargeServiceImpl implements ItemChargeService {

    @Autowired
    private ItemChargeMapper itemChargeMapper;

    @Override
    public ItemCharge selectByName(String name) {
        return itemChargeMapper.selectByName(name);
    }
}
