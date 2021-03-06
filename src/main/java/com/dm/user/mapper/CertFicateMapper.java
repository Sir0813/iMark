package com.dm.user.mapper;

import com.dm.user.entity.CertFicate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface CertFicateMapper {

    /**
     * 根据ID删除
     *
     * @param certId
     * @return
     */
    int deleteByPrimaryKey(Integer certId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(CertFicate record);

    /**
     * 根据ID查询
     *
     * @param certId
     * @return
     */
    CertFicate selectByPrimaryKey(Integer certId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CertFicate record);

    /**
     * 获取集合
     *
     * @param map
     * @return
     */
    List<CertFicate> list(Map<String, Object> map);

    /**
     * 根据ID批量查询
     *
     * @param map
     * @return
     */
    List<CertFicate> selectByIDs(Map<String, Object> map);

    /**
     * 根据ID修改撤回原因
     *
     * @param certId
     */
    void updateCertRevoke(int isRevoke, int certId);

    /**
     * 根据存证ID修改
     *
     * @param certId
     */
    void updateReasonByCertId(int isReturn, int certId);

    /**
     * 修改存证状态
     *
     * @param map
     */
    void updateCertState(Map<String, Object> map);

    /**
     * 根据ID和状态查询
     *
     * @param certId
     * @return
     */
    CertFicate selectByIdAndState(int certSuccess, int fileNotDelete, Integer certId);

    /**
     * 根据存证ID批量查询
     *
     * @param certIds
     * @return
     */
    List<CertFicate> selectByCertIDs(String[] certIds);

    /**
     * 我的存证
     *
     * @param map
     */
    List<CertFicate> myCertList(Map<String, Object> map);

    /**
     * 存证code获取存证
     *
     * @param certCode
     * @return
     */
    CertFicate selectByCertCode(String certCode);
}