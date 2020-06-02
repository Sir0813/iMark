package com.dm.user.service.impl;

import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfInstAuditTrack;
import com.dm.user.mapper.WfInstAuditTrackMapper;
import com.dm.user.service.WfInstAuditTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
