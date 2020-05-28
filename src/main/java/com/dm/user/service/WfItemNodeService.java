package com.dm.user.service;

import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.ProgressView;
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

    /**
     * 删除之前模板
     *
     * @param userId
     * @param applyId
     */
    void deleteByUserId(String userId, int applyId) throws Exception;

    /**
     * 新增
     *
     * @param wfItemNode
     */
    void insertData(WfItemNode wfItemNode);

    /**
     * 未审批节点
     *
     * @param applyid
     * @return
     */
    List<ProgressView> isNotProgress(Integer applyid);
}
