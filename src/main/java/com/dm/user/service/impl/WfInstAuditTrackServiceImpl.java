package com.dm.user.service.impl;

import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfInstAuditTrack;
import com.dm.user.mapper.WfInstAuditTrackMapper;
import com.dm.user.service.WfInstAuditTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfInstAuditTrackServiceImpl implements WfInstAuditTrackService {

    @Autowired
    private WfInstAuditTrackMapper wfInstAuditTrackMapper;

    @Override
    public void insertData(Map<String, Object> map) throws Exception {
        wfInstAuditTrackMapper.insertData(map);
    }

    @Override
    public void insertApproved(Map<String, Object> map) throws Exception {
        wfInstAuditTrackMapper.insertApproved(map);
    }

    @Override
    public List<ProgressView> selectIsProgress(Integer applyid) throws Exception {
        return wfInstAuditTrackMapper.selectIsProgress(applyid);
    }

    @Override
    public void insertNewData(Map<String, Object> map) throws Exception {
        wfInstAuditTrackMapper.insertNewData(map);
    }

    @Override
    public WfInstAuditTrack selectByInstanId(Map<String, Object> map) {
        return wfInstAuditTrackMapper.selectByInstanId(map);
    }

    @Override
    public void updateData(WfInstAuditTrack wfInstAuditTrack) {
        wfInstAuditTrackMapper.updateData(wfInstAuditTrack);
    }

    @Override
    public WfInstAuditTrack selectLastReason(int applyid) {
        return wfInstAuditTrackMapper.selectLastReason(applyid);
    }

    @Override
    public WfInstAuditTrack selectEditData(int applyid, String userId) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        map.put("userId", userId);
        return wfInstAuditTrackMapper.selectEditData(map);
    }

    @Override
    public WfInstAuditTrack selectById(Integer id) {
        return wfInstAuditTrackMapper.selectByPrimaryKey(id);
    }

    @Override
    public WfInstAuditTrack selectByApplyIdAndUserId(int applyid) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        return wfInstAuditTrackMapper.selectByApplyIdAndUserId(map);
    }

    @Override
    public WfInstAuditTrack selectByNodeId(Integer id) {
        return wfInstAuditTrackMapper.selectByNodeId(id);
    }

    @Override
    public WfInstAuditTrack selectByApplyIdAndInstIdAndNodeId(String applyid, String wfInstanceId, Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        map.put("wfInstanceId", wfInstanceId);
        map.put("id", id);
        return wfInstAuditTrackMapper.selectByApplyIdAndInstIdAndNodeId(map);
    }
}
