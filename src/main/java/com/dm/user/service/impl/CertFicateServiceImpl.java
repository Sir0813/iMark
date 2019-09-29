package com.dm.user.service.impl;

import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.*;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.*;
import com.dm.user.util.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CertFicateServiceImpl<selectByPrimaryKey> implements CertFicateService {
	
	@Autowired
	private CertFicateMapper certFicateMapper;
	
	@Autowired
	private CertFilesService certFilesService;
	
	@Autowired
	private CertConfirmService certConfirmService;

	@Autowired
	private UserService userService;

	@Autowired
	private TemFileMapper temFileMapper;

	@Autowired
	private PushMsgService pushMsgService;

	@Autowired
	private CIDService cidService;

	@Autowired
	private CertFicateService certFicateService;

	@Autowired
	private PdfConvertUtil pdfConvertUtil;

	@Override
	public CertFicate saveCert(CertFicate certFicate) throws Exception {
		try {
			boolean sendMsg = false;
            certFicate.setCertOwner(LoginUserHelper.getUserId());
			certFicate.setCertIsDelete(1);
			List<CertFiles> certFiles = null;
			// 模板存证
			if (certFicate.getCertType()==7){
				if (certFicate.getCertStatus()==StateMsg.TO_CERT){
					TemFile temFile = certFicateService.selectByCertId(certFicate.getCertId().toString());
					Integer fileId = pdfConvertUtil.acceptPage(temFile.getTemFileText(), temFile.getCertId());
					certFicate.setCertFilesid(fileId.toString());
					certFiles = getHash(certFicate);
				}
			}else{
				certFiles = getHash(certFicate);
			}
			certFicate.setCertPostDate(new Date());
			if (null!=certFicate.getCertId()){
				certFicateMapper.updateByPrimaryKeySelective(certFicate);
			}else{
				certFicateMapper.insertSelective(certFicate);
			}
			// 存证入链
			if (StateMsg.TO_CERT==certFicate.getCertStatus()) {
				// 对接存证sdk
                String dataHash = CryptoHelper.hash(certFicate.getCertHash()+certFicate.getCertId());
				Result result = cidService.save(dataHash, certFicate.getCertName(), DateUtil.timeToString2(new Date()), "");
				if (result.getData() instanceof TransactionResult) {
					TransactionResult tr = (TransactionResult) result.getData();
					certFicate.setCertChainno(tr.getTransactionID());
					certFicate.setCertBlockNumber(String.valueOf(tr.getBlockNumber()));
				}else{
					throw new Exception(result.getMsg());
				}
				certFicate.setCertDate(new Date());
				certFicate.setCertStatus(certFicate.getCertIsconf()==1?StateMsg.OTHERS_CONFIRM:StateMsg.CERT_SUCCESS);
				sendMsg = true;
			}else {
				certFicate.setCertStatus(StateMsg.NO_CERT);
			}
			DecimalFormat df = new DecimalFormat("000000");
			String ID = df.format(certFicate.getCertId());
			certFicate.setCertCode("DMS01"+ID);
			certFicateMapper.updateByPrimaryKeySelective(certFicate);
			/*需要他人确认*/
			if (certFicate.getCertIsconf()==1) {
				certIsConfirm(LoginUserHelper.getUserName(),sendMsg,certFicate,Integer.parseInt(LoginUserHelper.getUserId()));
			}
			if (null!=certFiles){
				for (int i = 0; i < certFiles.size(); i++) {
					CertFiles cf =  certFiles.get(i);
					cf.setCertId(certFicate.getCertId());
					certFilesService.updateByPrimaryKeySelective(cf);
					/*文件不保存至证云链 将文件删除*/
					if("0".equals(certFicate.getCertFileIsSave())){
						File file = new File(cf.getFilePath());
						file.delete();
						certFilesService.deleteByCertId(certFicate.getCertId());
					}
				}
			}
		}catch (Exception e){
			throw new Exception(e);
		}
		return certFicate;
	}

	private List<CertFiles> getHash(CertFicate certFicate) throws Exception{
		try {
			String[] filesId = certFicate.getCertFilesid().split(",");
			List<CertFiles>certFiles = certFilesService.findByFilesIds(filesId);
			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < certFiles.size(); i++) {
				CertFiles certFile =  certFiles.get(i);
				String str = CryptoHelper.hash(ShaUtil.getFileByte(certFile.getFilePath()));
				hash.append(str);
				certFile.setFileHash(str);
				certFilesService.updateByPrimaryKeySelective(certFile);
			}
			if (certFiles.size()==1){
				certFicate.setCertHash(hash.toString());
			}else{
				String dataHash = CryptoHelper.hash(hash.toString());
				certFicate.setCertHash(dataHash);
			}
			return certFiles;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void certIsConfirm(String username,boolean sendMsg,CertFicate certFicate,Integer userId) throws Exception {
		try {
			/*添加发起人*/
			if (null!=certFicate.getCertId()){
				certConfirmService.deleteByCertId(certFicate.getCertId());
			}
			CertConfirm cc = new CertConfirm();
			cc.setConfirmDate(new Date());
			cc.setConfirmState(StateMsg.ORIGINATOR);
			cc.setConfirmPhone(username);
			cc.setConfirmPerson(username);
			cc.setUserId(userId);
			certFicate.getCertConfirmList().add(cc);
			for (CertConfirm certConfirm : certFicate.getCertConfirmList()) {
				certConfirm.setCertId(certFicate.getCertId());
				if (null==certConfirm.getConfirmState()) {
					certConfirm.setConfirmState(StateMsg.NO_CONFIRM);
				}
				User u = userService.findByName(certConfirm.getConfirmPhone());
				if (null!=u){
					certConfirm.setUserId(u.getUserid());
				}
				if (sendMsg) {
					/*发消息请求确认*/
					if (certConfirm.getConfirmState()!=StateMsg.ORIGINATOR) {
						PushMsg pm = new PushMsg();
						pm.setCertFicateId(certFicate.getCertId().toString());
						pm.setTitle(StateMsg.SAVE_CERT_TITLE);
						pm.setContent(StateMsg.SAVE_CERT_CONTENT.replace("certName",certFicate.getCertName()));
						pm.setCertName(certFicate.getCertName());
						pm.setServerTime(DateUtil.timeToString2(new Date()));
						pm.setType("1");
						pm.setState("0");
						pm.setIsRead("0");
						pm.setReceive(certConfirm.getConfirmPhone());
						pushMsgService.insertSelective(pm);
						if (null!=u){
							pm.setUserId(u.getUserid());
							String json = new Gson().toJson(pm);
							int resout = PushUtil.getInstance().sendToRegistrationId(u.getUsername(), pm.getTitle(), json);
							if (1==resout){
								pm.setState("1");
							}
							pushMsgService.updateByPrimaryKeySelective(pm);
						}
					}
				}
				certConfirmService.insertSelective(certConfirm);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public CertFicate certDetails(String certFicateId) throws Exception {
		try {
			CertFicate certFicate = certFicateMapper.selectByPrimaryKey(Integer.parseInt(certFicateId));
			List<CertConfirm> list = certConfirmService.selectByCertId(Integer.parseInt(certFicateId));
			certFicate.setCertConfirmList(list);
			if (list.size()>0){
				list.forEach(cc->{
					if (null!=cc.getUserId()){
						if (cc.getUserId()==Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()==1){
							certFicate.setCertIsconf(1);//1待自己确认
							return;
						}
						if (cc.getUserId()!=Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()==1){
							certFicate.setCertIsconf(2);//1待他人确认
							return;
						}
					}else{
						if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getConfirmState()==1){
							certFicate.setCertIsconf(1);//1待自己确认
							return;
						}
						if (!cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getConfirmState()==1){
							certFicate.setCertIsconf(2);//1待他人确认
							return;
						}
					}
				});
			}
			if("1".equals(certFicate.getCertFileIsSave())){
				if (StringUtils.isNotBlank(certFicate.getCertFilesid())){
					String[] filesId = certFicate.getCertFilesid().split(",");
					List<CertFiles> certFiles = certFilesService.findByFilesIds(filesId);
					certFicate.setCertFilesList(certFiles);
				}
			}
			return certFicate;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public PageInfo<CertFicate> listCerts(Page<CertFicate>page,String state,String certName) throws Exception {
		try {
			List<CertFicate>list = new ArrayList<>();
			Map<String,Object>map = new HashMap<>(16);
			Integer [] ids = null;
			List<CertConfirm> confirmList = null;
			if (StringUtils.isBlank(state)){
				confirmList = certConfirmService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
			}else if("6".equals(state)){
			    //待自己确认
				confirmList = certConfirmService.selectByuserIdAndState(LoginUserHelper.getUserId(),"1");
			}
			if (confirmList!=null&&confirmList.size()>0){
				ids = new Integer[confirmList.size()];
				for (int i = 0; i < confirmList.size(); i++) {
					ids[i]=confirmList.get(i).getCertId();
				}
			}
			if (StringUtils.isBlank(certName)){
				map.put("certid",ids);
			}
			map.put("state", "null".equals(state)?"":state);
			map.put("userId",LoginUserHelper.getUserId());
			map.put("certName",certName);
			// 待自己确认
			if (state!=null&&"6".equals(state)){
				if (ids==null) {
					return null;
				}
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
				list = certFicateMapper.selectByIDs(ids);
			}else{
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
				list = certFicateMapper.list(map);
				certConfirmState(list,state);
			}
			if (list.size()==0) {
				return null;
			}
			PageInfo<CertFicate> pageInfo = new PageInfo<>(list);
			if (page.getPageNum()>pageInfo.getPages()) {
				return null;
			}
			return pageInfo;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void certConfirmState(List<CertFicate>list, String state) throws Exception {
		if (list.size()>0){
			Iterator<CertFicate> iterator = list.iterator();
			while (iterator.hasNext()){
				CertFicate l = iterator.next();
				if (l.getCertStatus()==StateMsg.OTHERS_CONFIRM){
					//待他人确认和待自己确认
					List<CertConfirm> certConfirms = certConfirmService.selectByCertId(l.getCertId());
					if (certConfirms.size()>0){
						certConfirms.forEach(cc->{
							if (null!=cc.getUserId()){
								// 用户确认人添加自己去除重复
								if (StringUtils.isNotBlank(state)){
									if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getUserId()==Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()==1){
										iterator.remove();
										return;
									}
								}
								if (cc.getUserId()==Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()==1){
									l.setCertIsconf(1);//1待自己确认
									return;
								}
								if (cc.getUserId()!=Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()==1){
									l.setCertIsconf(2);//1待他人确认
									return;
								}
							}else{
								if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getConfirmState()==1){
									l.setCertIsconf(1);//1待自己确认
									return;
								}
								if (!cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getConfirmState()==1){
									l.setCertIsconf(2);//1待他人确认
									return;
								}
							}
						});
					}
				}else if(l.getCertStatus()==6) {
					/*撤销的存证 确认人方不显示该存证*/
					List<CertConfirm> certConfirms = certConfirmService.selectByCertId(l.getCertId());
					if (certConfirms.size()>0){
						for (CertConfirm cc : certConfirms) {
							if (null!=cc.getUserId()){
								if (cc.getUserId()==Integer.parseInt(LoginUserHelper.getUserId())&&cc.getConfirmState()!=StateMsg.ORIGINATOR&&cc.getConfirmState()!=StateMsg.REVOKE_CONFIRM){
									iterator.remove();
									break;
								}
							}else{
								if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName())&&cc.getConfirmState()!=4){
									iterator.remove();
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void certRevoke(String certId) throws Exception {
		try {
			certFicateMapper.updateCertRevoke(Integer.parseInt(certId));
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
			map.put("confirmPhone", LoginUserHelper.getUserName());
			certConfirmService.updateByCertId(map);
			certFicateMapper.updateReasonByCertId(Integer.parseInt(map.get("certId").toString()));
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public void confirm(Map<String, Object> map) throws Exception {
		try {
			map.put("confirmPhone", LoginUserHelper.getUserName());
			certConfirmService.updateConfirmState(map);
			List<CertConfirm> confirmList = certConfirmService.selectByState(map);
			if (confirmList.size()==0) {
				certFicateMapper.updateCertState(map);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

    @Override
    public ByteArrayResource getCertImg(String certId) throws Exception {
	    try {
            CertFicate certFicate = certFicateMapper.selectByPrimaryKey(Integer.parseInt(certId));
            certFicate.setCertOwner(LoginUserHelper.getUserName());
			String os = System.getProperty("os.name").toLowerCase();
			String qrCodePath = "";
			String templatePath = "";
			String fileSuffix = ".png";
			String fileName = UUID.randomUUID().toString()+fileSuffix;
			if(!os.startsWith(StateMsg.OS_NAME)){
				qrCodePath = "/opt/czt-upload/"+fileName;
				templatePath = "/opt/czt-upload/certTemplate/ct.png";
			}else{
				qrCodePath = "D:\\upload\\"+fileName;
				templatePath = "D:\\ct.png";
			}
            QRCodeGenerator.generateQRCodeImage(certFicate.getCertCode(),qrCodePath);
            ByteArrayResource mark = CertImgUtil.createStringMark(certFicate, templatePath, qrCodePath);
            return mark;
        }catch (Exception e){
	        throw new Exception(e);
        }
    }

	@Override
	public CertFicate saveTemplate(TemCertFile temCertFile) throws Exception {
		try {
            CertFicate certFicate = null;
			if(null!=temCertFile.getTemFile()&&null!=temCertFile.getTemFile().getCertId()){
				TemFile temFile = temFileMapper.selectByCertId(temCertFile.getTemFile().getCertId().toString());
				temCertFile.getTemFile().setTemId(temFile.getTemId());
				temFileMapper.updateByPrimaryKeySelective(temCertFile.getTemFile());
				certFicate = certFicateMapper.selectByPrimaryKey(temCertFile.getTemFile().getCertId());
			}else{
				certFicate = new CertFicate();
				TemFile temFile = new TemFile();
				certFicate.setCertOwner(LoginUserHelper.getUserId());
				certFicate.setCertFileIsSave("1");
				certFicate.setCertIsDelete(1);
				certFicate.setCertStatus(0);
				certFicate.setCertName("模板存证");
				certFicate.setCertType(7);
				certFicateMapper.insertSelective(certFicate);
				temFile.setCertId(certFicate.getCertId());
				temFile.setTemFileText(temCertFile.getTemFile().getTemFileText());
				temFileMapper.insertSelective(temFile);
				certFicate.setTemId(temFile.getTemId());
			}
			return certFicate;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public TemFile selectByCertId(String certId) throws Exception {
		try {
			TemFile temFile = temFileMapper.selectByCertId(certId);
			return temFile;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public CertFicate selectByIdAndState(Integer certId) throws Exception {
		try {
			return certFicateMapper.selectByIdAndState(certId);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertFicate> selectByCertIDs(String[] split) throws Exception {
		try {
			return certFicateMapper.selectByCertIDs(split);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public CertFicate selectByPrimaryKey(Integer certId) throws Exception {
		try {
			return certFicateMapper.selectByPrimaryKey(certId);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
