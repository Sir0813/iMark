package com.dm.user.service;

import com.dm.user.entity.ApplyFileLog;

import java.util.List;

public interface ApplyFileLogService {

    /**
     * 新增
     *
     * @param applyFileLog
     */
    void insertData(ApplyFileLog applyFileLog);

    /**
     * 批注记录
     *
     * @param id
     * @return
     */
    List<ApplyFileLog> selectByFileId(int id);
}
