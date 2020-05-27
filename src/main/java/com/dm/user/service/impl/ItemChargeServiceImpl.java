package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ApplyFee;
import com.dm.user.entity.ItemCharge;
import com.dm.user.mapper.ItemChargeMapper;
import com.dm.user.service.ItemChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemChargeServiceImpl implements ItemChargeService {

    @Autowired
    private ItemChargeMapper itemChargeMapper;

    @Override
    public ItemCharge selectByName(String name) {
        return itemChargeMapper.selectByName(name);
    }

    @Override
    public Result selectByOrgIdAndState(Map<String, Object> map) {
        List<ApplyFee> itemCharges = itemChargeMapper.selectByOrgIdAndState(map);
        return ResultUtil.success(itemCharges);
    }
}
