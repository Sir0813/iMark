package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizItemVideo;
import com.dm.user.mapper.BizItemVideoMapper;
import com.dm.user.service.BizItemVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BizItemVideoServiceImpl implements BizItemVideoService {

    @Autowired
    private BizItemVideoMapper bizItemVideoMapper;

    @Override
    public Result insertData(BizItemVideo bizItemVideo) throws Exception {
        if (bizItemVideo.getId() != null) {
            bizItemVideoMapper.updateByPrimaryKeySelective(bizItemVideo);
        } else {
            bizItemVideo.setCreateTime(new Date());
            bizItemVideo.setIsConversation(0);
            bizItemVideoMapper.insertSelective(bizItemVideo);
        }
        return ResultUtil.success();
    }

    @Override
    public List<BizItemVideo> selectByApplyId(int applyid) throws Exception {
        List<BizItemVideo> bizItemVideoList = bizItemVideoMapper.selectByApplyId(applyid);
        return bizItemVideoList;
    }
}
