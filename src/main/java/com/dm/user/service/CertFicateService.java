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

	CertFicate saveCert(CertFicate certFicate) throws Exception;

	PageInfo<CertFicate> listCerts(Page<CertFicate> page,String state,String certName) throws Exception;

	CertFicate certDetails(String certFicateId) throws Exception;

	void draftDel(CertFicate certFicate) throws Exception;

	void certRevoke(String certId) throws Exception;

	void returnReason(Map<String,Object>map) throws Exception;

	void confirm(Map<String, Object> map) throws Exception;

    ByteArrayResource getCertImg(String certId) throws Exception;

	CertFicate saveTemplate(TemCertFile temCertFile) throws Exception;

	TemFile selectByCertId(String certId) throws Exception;

    CertFicate selectByIdAndState(Integer certId) throws Exception;

	List<CertFicate> selectByCertIDs(String[] split) throws Exception;

	CertFicate selectByPrimaryKey(Integer certId) throws Exception;
}
