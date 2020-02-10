package com.dm.user.service;

import com.dm.user.entity.CertFiles;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertFilesService {

    /**
     * 新增
     *
     * @param certFiles
     * @throws Exception
     */
    void insertSelective(CertFiles certFiles) throws Exception;

    /**
     * 修改
     *
     * @param cf
     * @throws Exception
     */
    void updateByPrimaryKeySelective(CertFiles cf) throws Exception;

    /**
     * 根据存证ID删除
     *
     * @param certId
     * @throws Exception
     */
    void deleteByCertId(Integer certId) throws Exception;

    /**
     * 根据文件ID批量查询
     *
     * @param filesId
     * @return
     * @throws Exception
     */
    List<CertFiles> findByFilesIds(String[] filesId) throws Exception;

    /**
     * 根据文件ID批量查询
     *
     * @param filesId
     * @return
     * @throws Exception
     */
    List<CertFiles> findByFilesIds2(String[] filesId) throws Exception;

    /**
     * 根据主键ID删除
     *
     * @param fileId
     * @throws Exception
     */
    void deleteByPrimaryKey(Integer fileId) throws Exception;

    /**
     * 根据主键ID查询
     *
     * @param fileId
     * @return
     * @throws Exception
     */
    CertFiles selectByPrimaryKey(Integer fileId) throws Exception;

    /**
     * 根据文件路径查询
     *
     * @param frontPath
     * @return
     * @throws Exception
     */
    CertFiles selectByUrl(String frontPath) throws Exception;
}
