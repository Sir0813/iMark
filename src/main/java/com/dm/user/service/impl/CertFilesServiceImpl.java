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
        try {
            certFilesMapper.insertSelective(certFiles);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void updateByPrimaryKeySelective(CertFiles cf) throws Exception {
        try {
            certFilesMapper.updateByPrimaryKeySelective(cf);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteByCertId(Integer certId) throws Exception {
        try {
            certFilesMapper.deleteByCertId(certId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<CertFiles> findByFilesIds(String[] filesId) throws Exception {
        try {
            return certFilesMapper.findByFilesIds(filesId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteByPrimaryKey(Integer fileId) throws Exception {
        try {
            certFilesMapper.deleteByPrimaryKey(fileId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public CertFiles selectByPrimaryKey(Integer fileId) throws Exception {
        try {
            return certFilesMapper.selectByPrimaryKey(fileId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public CertFiles selectByUrl(String frontPath) throws Exception {
        try {
            return certFilesMapper.selectByUrl(frontPath);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
