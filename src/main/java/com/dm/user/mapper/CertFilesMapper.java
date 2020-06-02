package com.dm.user.mapper;

import com.dm.user.entity.CertFiles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface CertFilesMapper {
    /**
     * 根据ID修改
     *
     * @param fileId
     * @return
     */
    int deleteByPrimaryKey(Integer fileId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(CertFiles record);

    /**
     * 根据ID查询
     *
     * @param fileId
     * @return
     */
    CertFiles selectByPrimaryKey(Integer fileId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CertFiles record);

    /**
     * 根据文件ID批量查询
     *
     * @param filesId
     * @return
     */
    List<CertFiles> findByFilesIds(String[] filesId);

    /**
     * 根据文件ID批量查询
     *
     * @param filesId
     * @return
     */
    List<CertFiles> findByFilesIds2(String[] filesId);

    /**
     * 根据存证ID删除
     *
     * @param certId
     */
    void deleteByCertId(Integer certId);

    /**
     * 根据URL查询
     *
     * @param frontPath
     * @return
     */
    CertFiles selectByUrl(String frontPath);

    /**
     * 修改材料
     *
     * @param map
     */
    List<CertFiles> selectUpdateFiles(Map<String, Object> map);

    /**
     * 添加材料
     *
     * @param map
     * @return
     */
    List<CertFiles> selectAddFiles(Map<String, Object> map);

    /**
     * 已提交修改材料
     *
     * @param applyid
     * @return
     */
    List<CertFiles> selectIsAddFiles(int applyid);

    /**
     * 已修改材料
     *
     * @param applyid
     * @return
     */
    List<CertFiles> selectIsUpdateFiles(int applyid);
}