package com.dm.user.service.impl;

import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.service.CertFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CertFilesServiceImpl implements CertFilesService {

    @Autowired
    private CertFilesMapper certFilesMapper;

    @Override
    public void insertSelective(CertFiles certFiles) throws Exception {
        certFilesMapper.insertSelective(certFiles);
    }

    @Override
    public void updateByPrimaryKeySelective(CertFiles cf) throws Exception {
        certFilesMapper.updateByPrimaryKeySelective(cf);
    }

    @Override
    public void deleteByCertId(Integer certId) throws Exception {
        certFilesMapper.deleteByCertId(certId);
    }

    @Override
    public List<CertFiles> findByFilesIds(String[] filesId) throws Exception {
        return certFilesMapper.findByFilesIds(filesId);
    }

    @Override
    public List<CertFiles> findByFilesIds2(String[] filesId) throws Exception {
        return certFilesMapper.findByFilesIds2(filesId);
    }

    @Override
    public void deleteByPrimaryKey(Integer fileId) throws Exception {
        certFilesMapper.deleteByPrimaryKey(fileId);
    }

    @Override
    public CertFiles selectByPrimaryKey(Integer fileId) throws Exception {
        return certFilesMapper.selectByPrimaryKey(fileId);
    }

    @Override
    public CertFiles selectByUrl(String frontPath) throws Exception {
        return certFilesMapper.selectByUrl(frontPath);
    }

}
