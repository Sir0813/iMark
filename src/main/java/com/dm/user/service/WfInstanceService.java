package com.dm.user.service;

import com.dm.user.entity.WfInstance;

import java.util.Map;

public interface WfInstanceService {

    /**
     * 新增
     *
     * @param wfInstance
     */
    void insert(WfInstance wfInstance) throws Exception;

    /**
     * 查询退回原因
     *
     * @param wfInstanceId
     * @return
     */
    WfInstance selectById(String wfInstanceId) throws Exception;

    /**
     * 主键修改
     *
     * @param wfInstance
     * @throws Exception
     */
    void updateById(WfInstance wfInstance) throws Exception;

    /**
     * 修改
     *
     * @param map
     */
    void updateByInstanceId(Map<String, Object> map) throws Exception;
}
