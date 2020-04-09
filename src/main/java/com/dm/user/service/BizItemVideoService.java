package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.BizItemVideo;

import java.util.List;

public interface BizItemVideoService {

    /**
     * 预约公正视频时间
     *
     * @param bizItemVideo
     * @return
     */
    Result insertData(BizItemVideo bizItemVideo) throws Exception;

    List<BizItemVideo> selectByApplyId(int applyid) throws Exception;
}
