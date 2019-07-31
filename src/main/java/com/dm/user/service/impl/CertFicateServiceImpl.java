package com.dm.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dm.cid.sdk.service.ChainCertService;
import com.dm.cid.sdk.service.impl.ChainCertServiceImpl;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.user.entity.CertConfirm;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertConfirmMapper;
import com.dm.user.mapper.CertFicateMapper;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.CertFicateService;
import com.dm.user.util.PushUtil;
import com.dm.user.util.ShaUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional(rollbackFor=Exception.class)
public class CertFicateServiceImpl implements CertFicateService{
	
	@Autowired
	private CertFicateMapper certFicateMapper;
	
	@Autowired
	private CertFilesMapper certFilesMapper;
	
	@Autowired
    private LoginUserService loginUserService;
	
	@Autowired
	private CertConfirmMapper certConfirmMapper;
	
	/*@Autowired
	private PushUtil pushUtil;*/

	@Override
	public CertFicate save(CertFicate certFicate) throws Exception {
		try {
			boolean sendMsg = false;
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LoginUserDetails userDetails = loginUserService.getUserByUsername(username);
			certFicate.setCertOwner(userDetails.getUserid());
			certFicate.setCertType(1);
			certFicate.setCertIsDelete(1);
			String[] filesId = certFicate.getCertFilesid().split(",");
			List<CertFiles>certFiles = certFilesMapper.findByFilesIds(filesId);
			/*Collections.sort(certFiles, new Comparator<CertFiles>() {  
			    @Override  
			    public int compare(CertFiles o1, CertFiles o2) {  
			        if (Integer.valueOf(o1.getFileSeq()) > Integer.valueOf(o2.getFileSeq())) {  
			            return 1;  
			        }  
			        if (Integer.valueOf(o1.getFileSeq()) == Integer.valueOf(o2.getFileSeq())) {  
			            return 0;  
			        }  
			        return -1;  
			    }  
			});*/
			StringBuffer hash = new StringBuffer();
			certFiles.forEach(certFile ->{
				String sha256 = ShaUtil.SHA(ShaUtil.encodeBase64File(certFile.getFilePath()).getBytes());
				hash.append(sha256);
			});
			if (certFiles.size()==1) {
				certFicate.setCertHash(hash.toString());
			}else {
				String dataHash = ShaUtil.SHA(hash.toString().getBytes());
				certFicate.setCertHash(dataHash);
			}
			certFicate.setCertPostDate(new Date());
			/*存证入链*/
			if (StateMsg.toCert==certFicate.getCertStatus()) {
				/*ChainCertService chainCertService = new ChainCertServiceImpl();
				Result result = chainCertService.save(certFicate.getCertHash(), certFicate.getCertName(), new Date().toString(), "");
				Object data = result.getData();
				if (data instanceof TransactionResult) {
					TransactionResult tr = (TransactionResult) result.getData();
	                String txid = tr.getTransactionID();
	                certFicate.setCertChainno(txid);
				}*/
				certFicate.setCertDate(new Date());
				certFicate.setCertStatus(certFicate.getCertIsconf()==1?StateMsg.othersConfirm:StateMsg.certSuccess);
				sendMsg = true;
			}else {
				certFicate.setCertStatus(StateMsg.noCert);
			}
			certFicateMapper.insertSelective(certFicate);
			if (certFicate.getCertIsconf()==1) {/*需要他人确认*/
				CertConfirm cc = new CertConfirm();
				cc.setConfirmDate(new Date());
				cc.setConfirmState(StateMsg.originator);
				cc.setConfirmPhone(username);
				cc.setConfirmPerson(username);
				certFicate.getCertConfirmList().add(cc);
				for (CertConfirm certConfirm : certFicate.getCertConfirmList()) {
					certConfirm.setCertId(certFicate.getCertId());
					if (null==certConfirm.getConfirmState()) {
						certConfirm.setConfirmState(StateMsg.noConfirm);
					}
					if (sendMsg) {/*存证发送短信 存草稿不发信息*/
						//LoginUserDetails user = loginUserService.getUserByUsername(certConfirm.getConfirmPhone());
						/*发消息请求确认*/
						if (certConfirm.getConfirmState()!=StateMsg.originator) {
							JSONObject json = new JSONObject();
							json.put("time", "2019");
							json.put("type", "0");
							json.put("url", "www.baidu.com");
							PushUtil.getInstance().sendToRegistrationId("170976fa8af4d7f63a0", "我是标题", json.toString());
						}
					}
					certConfirmMapper.insertSelective(certConfirm);
				}
			}
			certFiles.forEach(cf ->{
				cf.setCertId(certFicate.getCertId().toString());
				certFilesMapper.updateByPrimaryKeySelective(cf);
			});
		} catch (Exception e) {
			throw new Exception(e);
		}
		return certFicate;
	}
	
	@Override
	public CertFicate details(Integer certFicateId) throws Exception {
		try {
			CertFicate certFicate = certFicateMapper.selectByPrimaryKey(certFicateId);
			List<CertConfirm> list = certConfirmMapper.selectByCertId(certFicateId);
			certFicate.setCertConfirmList(list);
			String[] filesId = certFicate.getCertFilesid().split(",");
			List<CertFiles> certFiles = certFilesMapper.findByFilesIds(filesId);
			certFicate.setCertFilesList(certFiles);
			return certFicate;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public PageInfo<CertFicate> list(Page<CertFicate>page,String state) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LoginUserDetails userDetails = loginUserService.getUserByUsername(username);
			Map<String,Object>map = new HashMap<>();
			map.put("state", "null".equals(state)?"":state);
			map.put("userId",Integer.parseInt(userDetails.getUserid()));
			PageHelper.startPage(page.getPageNum(), StateMsg.pageSize, "cert_post_date DESC");
			List<CertFicate>certFicateList = certFicateMapper.list(map);
			PageInfo<CertFicate> pageInfo = new PageInfo<CertFicate>(certFicateList);
			if (page.getPageNum()>pageInfo.getPages())
			return null;
			return pageInfo;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public PageInfo<CertFicate> waitMyselfConfirm(Page<CertFicate> page) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<CertConfirm> confirmList = certConfirmMapper.selectByConfirmPhone(username);
			Integer [] ids = new Integer[confirmList.size()];
			for (int i = 0; i < confirmList.size(); i++) {
				ids[i]=confirmList.get(i).getCertId();
			}
			if (ids.length==0) return null;
			PageHelper.startPage(page.getPageNum(), StateMsg.pageSize, "cert_post_date DESC");
			List<CertFicate> list = certFicateMapper.selectByIDs(ids);
			PageInfo<CertFicate> pageInfo = new PageInfo<CertFicate>(list);
			if(page.getPageNum()>pageInfo.getPages()) return null;
			return pageInfo;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public void revoke(int certId) throws Exception {
		try {
			certFicateMapper.updateCertRevoke(certId);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public void draftDel(CertFicate certFicate) throws Exception {
		try {
			certFicate.setCertDelDate(new Date());
			certFicateMapper.updateByCertId(certFicate);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public void returnReason(Map<String,Object>map) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			map.put("confirmPhone", username);
			certConfirmMapper.updateByCertId(map);
			certFicateMapper.updateReasonByCertId(Integer.parseInt(map.get("certId").toString()));
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public void confirm(Map<String, Object> map) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			map.put("confirmPhone", username);
			certConfirmMapper.updateConfirmState(map);
			List<CertConfirm> confirmList = certConfirmMapper.selectByState(map);
			if (confirmList.size()==0) {
				certFicateMapper.updateCertState(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
