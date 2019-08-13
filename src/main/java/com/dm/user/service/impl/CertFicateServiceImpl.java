package com.dm.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.user.entity.*;
import com.dm.user.mapper.CertConfirmMapper;
import com.dm.user.mapper.CertFicateMapper;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.CertFicateService;
import com.dm.user.service.PushMsgService;
import com.dm.user.util.PushUtil;
import com.dm.user.util.ShaUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class CertFicateServiceImpl implements CertFicateService {
	
	@Autowired
	private CertFicateMapper certFicateMapper;
	
	@Autowired
	private CertFilesMapper certFilesMapper;
	
	@Autowired
    private LoginUserService loginUserService;
	
	@Autowired
	private CertConfirmMapper certConfirmMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PushMsgService pushMsgService;

	@Autowired
	private CIDService cidService;

	private static Logger logger = LoggerFactory.getLogger(CertFicateServiceImpl.class);

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
			certFiles.forEach(certFile->{
				String str = CryptoHelper.hash(ShaUtil.getFileByte(certFile.getFilePath()));
				hash.append(str);
			});
			if (certFiles.size()==1){
				certFicate.setCertHash(hash.toString());
			}else{
				String dataHash = CryptoHelper.hash(hash.toString());
				certFicate.setCertHash(dataHash);
			}
			certFicate.setCertPostDate(new Date());
			/*存证入链*/
			if (StateMsg.toCert==certFicate.getCertStatus()) {
				/*对接存证sdk*/
				Result result = null;
				Object data = null;
				try {
					result = cidService.save(certFicate.getCertHash(), certFicate.getCertName(), new Date().toString(), "");
				} catch (Exception e) {
					data = result.getData();
					JSONObject json = JSONObject.parseObject(data.toString());
					logger.error("[存证sdk]error msg: "+json.get("msg"));
					logger.error("[存证sdk]error code: "+json.get("code"));
					logger.error("[存证sdk]error data: "+json.get("data"));
				}
				if (data instanceof TransactionResult) {
					TransactionResult tr = (TransactionResult) result.getData();
					String txid = tr.getTransactionID();
					certFicate.setCertChainno(txid);
				}
				certFicate.setCertDate(new Date());
				certFicate.setCertStatus(certFicate.getCertIsconf()==1?StateMsg.othersConfirm:StateMsg.certSuccess);
				sendMsg = true;
			}else {
				certFicate.setCertStatus(StateMsg.noCert);
			}
			certFicateMapper.insertSelective(certFicate);
			/*需要他人确认*/
			if (certFicate.getCertIsconf()==1) {
				/*添加发起人*/
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
					if (sendMsg) {
						/*发消息请求确认*/
						if (certConfirm.getConfirmState()!=StateMsg.originator) {
							User u = userMapper.findByUserName(certConfirm.getConfirmPhone());
							if (null!=u){
								PushMsg pm = new PushMsg();
								pm.setCertFicateId(certFicate.getCertId().toString());
								pm.setTitle("存证待确认→");
								pm.setContent("您有一条【"+certFicate.getCertName()+"】的存证待确认");
								pm.setCertName(certFicate.getCertName());
								pm.setServerTime(new Date());
								pm.setType("1");
								pm.setState("0");
								pm.setReceive(u.getUsername());
								String json = new Gson().toJson(pm);
								int resout = PushUtil.getInstance().sendToRegistrationId(u.getUsername(), pm.getTitle(), json);
								if (resout==1){
									pm.setState("1");
								}
								pushMsgService.insertSelective(pm);
							}
						}
					}
					certConfirmMapper.insertSelective(certConfirm);
				}
			}
			certFiles.forEach(cf->{
				cf.setCertId(certFicate.getCertId());
				certFilesMapper.updateByPrimaryKeySelective(cf);
				/*文件不保存至证云链 将文件删除*/
				if(certFicate.getCertFileIsSave().equals("0")){
					File file = new File(cf.getFilePath());
					file.delete();
					certFilesMapper.deleteByCertId(certFicate.getCertId());
				}
			});
		}catch (Exception e){
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
			if(certFicate.getCertFileIsSave().equals("1")){
				String[] filesId = certFicate.getCertFilesid().split(",");
				List<CertFiles> certFiles = certFilesMapper.findByFilesIds(filesId);
				certFicate.setCertFilesList(certFiles);
			}
			return certFicate;
		} catch (Exception e) {
			throw new Exception();
		}
	}
	
	@Override
	public PageInfo<CertFicate> list(Page<CertFicate>page,String state) throws Exception {
		try {
			List<CertFicate>list = null;
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LoginUserDetails userDetails = loginUserService.getUserByUsername(username);
			Map<String,Object>map = new HashMap<>();
			Integer [] ids = null;
			if (StringUtils.isBlank(state)||state.equals("6")){
				List<CertConfirm> confirmList = certConfirmMapper.selectByConfirmPhone(username);
				if (confirmList.size()>0){
					ids = new Integer[confirmList.size()];
					for (int i = 0; i < confirmList.size(); i++) {
						ids[i]=confirmList.get(i).getCertId();
					}
				}
			}
			map.put("certid",ids);
			map.put("state", "null".equals(state)?"":state);
			map.put("userId",Integer.parseInt(userDetails.getUserid()));
			PageHelper.startPage(page.getPageNum(), StateMsg.pageSize, "cert_post_date DESC");
			if (state.equals("6")){
				if (ids==null){
					return null;
				}else{
					list = certFicateMapper.selectByIDs(ids);
				}
			}else{
				list = certFicateMapper.list(map);
			}
			if (list.size()==0) return null;
			PageInfo<CertFicate> pageInfo = new PageInfo<>(list);
			if (page.getPageNum()>pageInfo.getPages()) return null;
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
