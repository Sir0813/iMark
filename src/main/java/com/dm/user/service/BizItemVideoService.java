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

    /**
     * 公正视频预约记录
     *
     * @param applyid
     * @param status
     * @return
     * @throws Exception
     */
    List<BizItemVideo> selectByApplyId(int applyid, int status) throws Exception;

}
