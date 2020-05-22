package com.dm.user.service;

import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.WfItemNode;

import java.util.List;

public interface WfItemNodeService {

    /**
     * 主键ID查询
     *
     * @param nodeid
     */
    WfItemNode selectById(Integer nodeid) throws Exception;

    /**
     * 流程模板
     *
     * @return
     * @throws Exception
     */
    List<ProcessConfig> selectByUserId() throws Exception;
}
