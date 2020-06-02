package com.dm.user.service;

import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfInstAuditTrack;

import java.util.List;
import java.util.Map;

public interface WfInstAuditTrackService {

    /**
     * 退回申请新增
     *
     * @param map
     */
    void insertData(Map<String, Object> map) throws Exception;

    /**
     * 审批通过新增
     *
     * @param map
     * @throws Exception
     */
    void insertApproved(Map<String, Object> map) throws Exception;

    /**
     * 已审批节点
     *
     * @param applyid
     * @return
     */
    List<ProgressView> selectIsProgress(Integer applyid) throws Exception;

    /**
     * 新增数据
     *
     * @param map
     */
    void insertNewData(Map<String, Object> map) throws Exception;

    /**
     * 查询草稿
     *
     * @param map
     */
    WfInstAuditTrack selectByInstanId(Map<String, Object> map);

    /**
     * 修改信息
     *
     * @param wfInstAuditTrack
     */
    void updateData(WfInstAuditTrack wfInstAuditTrack);
}
