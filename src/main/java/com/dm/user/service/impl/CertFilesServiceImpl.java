package com.dm.user.service.impl;

import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.service.CertFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<CertFiles> selectUpdateFiles(int applyid, int fileType) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        map.put("fileType", fileType);
        return certFilesMapper.selectUpdateFiles(map);
    }

    @Override
    public List<CertFiles> selectAddFiles(int applyid, int fileType) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", applyid);
        map.put("fileType", fileType);
        return certFilesMapper.selectAddFiles(map);
    }

    @Override
    public List<CertFiles> selectIsAddFiles(int applyid) {
        return certFilesMapper.selectIsAddFiles(applyid);
    }

    @Override
    public List<CertFiles> selectIsUpdateFiles(int applyid) {
        return certFilesMapper.selectIsUpdateFiles(applyid);
    }
}
