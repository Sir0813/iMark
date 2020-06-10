package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
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
        if (bizItemVideo.getId() > 0) {
            if (bizItemVideo.getIsDel() == 0) {
                bizItemVideo.setDelTime(new Date());
            }
            bizItemVideoMapper.updateByPrimaryKeySelective(bizItemVideo);
        } else {
            bizItemVideo.setIsDel(1);
            bizItemVideo.setCreateTime(new Date());
            bizItemVideo.setIsConversation(0);
            bizItemVideo.setCreatedBy(Integer.parseInt(LoginUserHelper.getUserId()));
            bizItemVideoMapper.insertSelective(bizItemVideo);
        }
        return ResultUtil.success();
    }

    @Override
    public List<BizItemVideo> selectByApplyId(int applyid, int status) throws Exception {
        List<BizItemVideo> bizItemVideoList = bizItemVideoMapper.selectByApplyId(applyid, status);
        return bizItemVideoList;
    }

}
