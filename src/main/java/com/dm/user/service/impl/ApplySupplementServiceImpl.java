package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ApplySupplement;
import com.dm.user.mapper.ApplySupplementMapper;
import com.dm.user.service.ApplySupplementService;
import com.dm.user.service.ItemApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApplySupplementServiceImpl implements ApplySupplementService {

    @Autowired
    private ApplySupplementMapper applySupplementMapper;

    @Autowired
    private ItemApplyService itemApplyService;

    @Override
    public Result add(ApplySupplement applySupplement) throws Exception {
        applySupplement.setCreateTime(new Date());
        applySupplementMapper.insertSelective(applySupplement);
        itemApplyService.updateById(applySupplement.getApplyid());
        return ResultUtil.success();
    }
}
