package com.dm.user.service;

import com.dm.user.entity.CertFiles;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertFilesService {

	void insertSelective(CertFiles certFiles) throws Exception;

    void updateByPrimaryKeySelective(CertFiles cf) throws Exception;

    void deleteByCertId(Integer certId) throws Exception;

    List<CertFiles> findByFilesIds(String[] filesId) throws Exception;

    void deleteByPrimaryKey(Integer fileId) throws Exception;

    CertFiles selectByPrimaryKey(Integer fileId) throws Exception;

    CertFiles selectByUrl(String frontPath) throws Exception;
}
