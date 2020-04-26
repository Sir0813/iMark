package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.PubDictory;
import com.dm.user.mapper.PubDictoryMapper;
import com.dm.user.service.PubDictoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubDictoryServiceImpl implements PubDictoryService {

    @Autowired
    private PubDictoryMapper pubDictoryMapper;

    @Override
    public Result selectCountry() throws Exception {
        List<PubDictory> pubDictoryList = pubDictoryMapper.selectCountry("COUNTRY");
        return ResultUtil.success(pubDictoryList);
    }

    @Override
    public Result selectCountryLanguage(Integer id) throws Exception {
        List<PubDictory> pubDictoryList = pubDictoryMapper.selectCountryLanguage(id);
        return ResultUtil.success(pubDictoryList);
    }
}
