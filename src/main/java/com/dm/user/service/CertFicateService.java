package com.dm.user.service;

import com.dm.user.entity.CertFicate;
import com.dm.user.entity.TemCertFile;
import com.dm.user.entity.TemFile;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface CertFicateService {

    /**
     * 存证
     *
     * @param certFicate
     * @return
     * @throws Exception
     */
    CertFicate saveCert(CertFicate certFicate) throws Exception;

    /**
     * 存证列表
     *
     * @param page
     * @param state    6 代表待自己确认
     * @param certName
     * @return
     * @throws Exception
     */
    // PageInfo<CertFicate> listCerts(Page<CertFicate> page, String state, String certName) throws Exception;

    /**
     * 存证详情
     *
     * @param certFicateId
     * @return
     * @throws Exception
     */
    CertFicate certDetails(String certFicateId) throws Exception;

    /**
     * 草稿删除 逻辑删除
     *
     * @param certFicate
     * @throws Exception
     */
    void draftDel(CertFicate certFicate) throws Exception;

    /**
     * 存证退回
     *
     * @param certId
     * @throws Exception
     */
    void certRevoke(String certId) throws Exception;

    /**
     * 退回待自己确认存证
     *
     * @param map certId 存证ID  reason  退回原因
     * @throws Exception
     */
    void returnReason(Map<String, Object> map) throws Exception;

    /**
     * 确认待自己确认
     *
     * @param map certId
     * @throws Exception
     */
    void confirm(Map<String, Object> map) throws Exception;

    /**
     * 获取存证证书
     *
     * @param certId
     * @return
     * @throws Exception
     */
    ByteArrayResource getCertImg(String certId) throws Exception;

    /**
     * 模板存证存草稿
     *
     * @param temCertFile
     * @return
     * @throws Exception
     */
    CertFicate saveTemplate(TemCertFile temCertFile) throws Exception;

    /**
     * 根据存证ID查询
     *
     * @param certId
     * @return
     * @throws Exception
     */
    TemFile selectByCertId(String certId) throws Exception;

    /**
     * 根据存证ID和状态查询
     *
     * @param certId
     * @return
     * @throws Exception
     */
    CertFicate selectByIdAndState(Integer certId) throws Exception;

    /**
     * 根据存证ID批量查询
     *
     * @param split
     * @return
     * @throws Exception
     */
    List<CertFicate> selectByCertIDs(String[] split) throws Exception;

    /**
     * 根据主键查询
     *
     * @param certId
     * @return
     * @throws Exception
     */
    CertFicate selectByPrimaryKey(Integer certId) throws Exception;

    /**
     * 我的存证
     *
     * @param page
     * @param state
     * @param certName
     * @return
     */
    PageInfo<CertFicate> myCertList(Page<CertFicate> page, String state, String certName) throws Exception;

    /**
     * 获取存证证书
     *
     * @param certCode
     * @return
     */
    ByteArrayResource getPubCertImg(String certCode) throws Exception;
}
