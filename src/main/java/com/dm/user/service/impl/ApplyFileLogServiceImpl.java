package com.dm.user.service.impl;

import com.dm.user.entity.ApplyFileLog;
import com.dm.user.mapper.ApplyFileLogMapper;
import com.dm.user.service.ApplyFileLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApplyFileLogServiceImpl implements ApplyFileLogService {

    @Autowired
    private ApplyFileLogMapper applyFileLogMapper;

    @Override
    public void insertData(ApplyFileLog applyFileLog) {
        applyFileLogMapper.insertSelective(applyFileLog);
    }

    @Override
    public List<ApplyFileLog> selectByFileId(int id) {
        return applyFileLogMapper.selectByFileId(id);
    }
}
