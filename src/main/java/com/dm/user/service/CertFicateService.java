package com.dm.user.service;

import com.dm.user.entity.CertFicate;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

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
	PageInfo<CertFicate> list(Page<CertFicate> page,String state) throws Exception;

	/**
	 * 存证详情
	 * @param certFicateId 存证ID
	 * @return
	 */
	CertFicate details(Integer certFicateId) throws Exception;

	/**
	 * 等待自己确认
	 * @return
	 */
	//PageInfo<CertFicate> waitMyselfConfirm(Page<CertFicate> page) throws Exception;

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
	void revoke(int certId) throws Exception;

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

}
