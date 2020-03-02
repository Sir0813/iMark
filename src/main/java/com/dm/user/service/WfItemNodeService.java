package com.dm.user.service;

import com.dm.user.entity.WfItemNode;

public interface WfItemNodeService {

    /**
     * 根据itemid查询流程集合
     *
     * @param itemid
     * @return
     */
    WfItemNode selectByItemId(Integer itemid) throws Exception;
}
