package com.dm.user.service;

import com.dm.user.entity.WfItemNode;

import java.util.Map;

public interface WfItemNodeService {

    /**
     * 根据itemid查询流程第一个节点
     *
     * @param itemid
     * @return
     */
    WfItemNode selectByItemId(Integer itemid) throws Exception;

    /**
     * 根据itemid查询流程最后一个节点
     *
     * @param itemid
     * @return
     * @throws Exception
     */
    WfItemNode selectByItemIdDesc(Integer itemid) throws Exception;

    /**
     * 主键ID查询
     *
     * @param nodeid
     */
    WfItemNode selectById(Integer nodeid) throws Exception;

    /**
     * 根据排序查询
     *
     * @param map
     * @return
     */
    WfItemNode selectByOrder(Map<String, Object> map) throws Exception;
}
