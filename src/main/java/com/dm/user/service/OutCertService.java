package com.dm.user.service;

import com.dm.user.entity.OutCert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface OutCertService {

    /**
     * 动态下载出证模板
     * @param certIds
     * @return
     * @throws Exception
     */
    String downloadOutCertTemplate(String certIds) throws Exception;

    /**
     * 保存
     * @param outCert
     * @throws Exception
     */
    void save(OutCert outCert) throws Exception;

    /**
     * 出证列表
     * @param page
     * @param state
     * @return
     * @throws Exception
     */
    PageInfo<OutCert> list(Page<OutCert> page, String state) throws Exception;

    /**
     * 出证详情
     * @param outCertId
     * @return
     * @throws Exception
     */
    OutCert details(String outCertId) throws Exception;

    /**
     * 出证说明及出证文件打包成ZIP
     * @param outCertId
     * @return
     * @throws Exception
     */
    String downZip(String outCertId) throws Exception;
}
