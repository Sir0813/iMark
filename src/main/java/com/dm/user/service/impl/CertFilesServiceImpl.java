package com.dm.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.service.CertFilesService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CertFilesServiceImpl implements CertFilesService {

    @Autowired
    private CertFilesMapper certFilesMapper;

    @Override
    public void insertSelective(CertFiles certFiles) throws Exception{
        try {
            certFilesMapper.insertSelective(certFiles);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
