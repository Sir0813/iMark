package com.dm.user.service;

import com.dm.user.entity.CertConfirm;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertConfirmService {

    /**
     * 根据ID删除
     *
     * @param certId
     * @throws Exception
     */
    void deleteByCertId(Integer certId) throws Exception;

    /**
     * 新增
     *
     * @param certConfirm
     * @throws Exception
     */
    void insertSelective(CertConfirm certConfirm) throws Exception;

    /**
     * 根据存证ID查询
     *
     * @param certId 存证ID
     * @return
     * @throws Exception
     */
    List<CertConfirm> selectByCertId(int certId) throws Exception;

    /**
     * 根据用户ID查询
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<CertConfirm> selectByUserId(int userId) throws Exception;

    /**
     * 根据用户ID和状态查询
     *
     * @param userId 用户状态
     * @param state  状态
     * @return
     * @throws Exception
     */
    List<CertConfirm> selectByuserIdAndState(String userId, String state) throws Exception;

    /**
     * 根据存证ID修改
     *
     * @param map reason 退回原因 certId 存证ID confirmPhone 确认人手机号
     * @throws Exception
     */
    void updateByCertId(Map<String, Object> map) throws Exception;

    /**
     * 修改确认状态
     *
     * @param map certId 存证ID confirmPhone 确认人手机号
     * @throws Exception
     */
    void updateConfirmState(Map<String, Object> map) throws Exception;

    /**
     * 根据状态 存证ID查询
     *
     * @param map certId 存证ID
     * @return
     * @throws Exception
     */
    List<CertConfirm> selectByState(Map<String, Object> map) throws Exception;

    /**
     * 根据手机号查询
     *
     * @param username 用户手机号
     * @return
     * @throws Exception
     */
    List<CertConfirm> selectByPhone(String username) throws Exception;

    /**
     * 修改
     *
     * @param certConfirm
     */
    void updateByPrimaryKeySelective(CertConfirm certConfirm);
}
