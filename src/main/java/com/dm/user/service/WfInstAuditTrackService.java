package com.dm.user.service;

import java.util.Map;

public interface WfInstAuditTrackService {

    /**
     * 退回申请新增
     *
     * @param map
     */
    void insertData(Map<String, Object> map) throws Exception;
}
