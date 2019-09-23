package com.dm.user.service;

import com.dm.user.entity.CertFicate;
import com.dm.user.entity.TemCertFile;
import com.dm.user.entity.TemFile;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.core.io.ByteArrayResource;

import java.util.Map;

public interface CertFicateService {

	/**
	 * 存证
	 * @param certFicate
	 * @throws Exception
	 */
	CertFicate save(CertFicate certFicate) throws Exception;

	/**
	 * 我的存证全部存证列表
	 * @return
	 * @throws Exception
	 */
	PageInfo<CertFicate> list(Page<CertFicate> page,String state,String certName) throws Exception;

	/**
	 * 存证详情
	 * @param certFicateId 存证ID
	 * @return
	 */
	CertFicate details(String certFicateId) throws Exception;

	/**
	 * 草稿删除
	 * @param certFicate
	 * @return
	 */
	void draftDel(CertFicate certFicate) throws Exception;

	/**
	 * 撤回待他人确认存证
	 * @param certId 存证ID
	 */
	void revoke(String certId) throws Exception;

	/**
	 * 退回待自己确认存证
	 * @param reason 原因
	 * @throws Exception 
	 */
	void returnReason(Map<String,Object>map) throws Exception;

	/**
	 * 确认待自己确认
	 * @param map
	 */
	void confirm(Map<String, Object> map) throws Exception;

	/**
	 * 获取证书文件
	 * @param certId
	 * @return
	 * @throws Exception
	 */
    ByteArrayResource getCertImg(String certId) throws Exception;

	/**
	 * 模板存证存草稿
	 * @param temFile
	 * @return
	 */
	CertFicate temSave(TemCertFile temCertFile) throws Exception;

	/**
	 * 获取模板草稿
	 * @return
	 * @throws Exception
	 */
	TemFile selectByCertId(String certId) throws Exception;
}
