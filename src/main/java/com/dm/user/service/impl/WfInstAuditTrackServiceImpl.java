package com.dm.user.service.impl;

import com.dm.user.mapper.WfInstAuditTrackMapper;
import com.dm.user.service.WfInstAuditTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class WfInstAuditTrackServiceImpl implements WfInstAuditTrackService {

    @Autowired
    private WfInstAuditTrackMapper wfInstAuditTrackMapper;

    @Override
    public void insertData(Map<String, Object> map) throws Exception {
        try {
            wfInstAuditTrackMapper.insertData(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void insertApproved(Map<String, Object> map) throws Exception {
        try {
            wfInstAuditTrackMapper.insertApproved(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
