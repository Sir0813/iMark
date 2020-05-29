package com.dm.user.service;

import com.dm.user.entity.BizCertModel;

import java.util.Map;

public interface BizCertService {

    /**
     * 根据itemid查询
     *
     * @param itemid
     * @return
     */
    BizCertModel selectByItemId(Integer itemid);

    /**
     * 文件占位符
     *
     * @param applyId
     */
    Map<String, Object> certInfo(Integer applyId);
}
