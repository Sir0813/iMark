package com.dm.user.service.impl;

import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.*;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.CertFicateService;
import com.dm.user.service.PushMsgService;
import com.dm.user.util.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class CertFicateServiceImpl implements CertFicateService {
	
	@Autowired
	private CertFicateMapper certFicateMapper;
	
	@Autowired
	private CertFilesMapper certFilesMapper;
	
	@Autowired
	private CertConfirmMapper certConfirmMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TemFileMapper temFileMapper;

	@Autowired
	private PushMsgService pushMsgService;

	@Autowired
	private CIDService cidService;

	@Autowired
	private CertFicateService certFicateService;

	@Autowired
	private PDFConvertUtil pdfConvertUtil;

	@Override
	public CertFicate save(CertFicate certFicate) throws Exception {
		try {
			boolean sendMsg = false;
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userMapper.findByName(username);
            certFicate.setCertOwner(user.getUserid().toString());
			certFicate.setCertIsDelete(1);
			List<CertFiles> certFiles = null;
			if (certFicate.getCertType()==7){//模板存证
				if (certFicate.getCertStatus()==StateMsg.toCert){
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
			/*存证入链*/
			if (StateMsg.toCert==certFicate.getCertStatus()) {
				/*对接存证sdk*/
                String dataHash = CryptoHelper.hash(certFicate.getCertHash()+certFicate.getCertId());
				Result result = null;
				try {
					result = cidService.save(dataHash, certFicate.getCertName(), DateUtil.timeToString2(new Date()), "");
				} catch (Exception e) {
					throw new Exception(e);
				}
				if (result.getData() instanceof TransactionResult) {
					TransactionResult tr = (TransactionResult) result.getData();
					String txid = tr.getTransactionID();
					certFicate.setCertChainno(txid);
				}else{
					throw new Exception(result.getMsg().toString());
				}
				certFicate.setCertDate(new Date());
				certFicate.setCertStatus(certFicate.getCertIsconf()==1?StateMsg.othersConfirm:StateMsg.certSuccess);
				sendMsg = true;
			}else {
				certFicate.setCertStatus(StateMsg.noCert);
			}
			DecimalFormat df = new DecimalFormat("000000");
			String ID = df.format(certFicate.getCertId());
			certFicate.setCertCode("DMS01"+ID);
			certFicateMapper.updateByPrimaryKeySelective(certFicate);
			/*需要他人确认*/
			if (certFicate.getCertIsconf()==1) {
				certIsConfirm(username,sendMsg,certFicate,user.getUserid());
			}
			if (null!=certFiles){
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
			}
		}catch (Exception e){
			throw new Exception(e);
		}
		return certFicate;
	}

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

	private List<CertFiles> getHash(CertFicate certFicate) throws Exception{
		try {
			String[] filesId = certFicate.getCertFilesid().split(",");
			List<CertFiles>certFiles = certFilesMapper.findByFilesIds(filesId);
			StringBuffer hash = new StringBuffer();
			certFiles.forEach(certFile->{
				String str = CryptoHelper.hash(ShaUtil.getFileByte(certFile.getFilePath()));
				hash.append(str);
				certFile.setFileHash(str);
				certFilesMapper.updateByPrimaryKeySelective(certFile);
			});
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
				certConfirmMapper.deleteByCertId(certFicate.getCertId());
			}
			CertConfirm cc = new CertConfirm();
			cc.setConfirmDate(new Date());
			cc.setConfirmState(StateMsg.originator);
			cc.setConfirmPhone(username);
			cc.setConfirmPerson(username);
			cc.setUserId(userId);
			certFicate.getCertConfirmList().add(cc);
			for (CertConfirm certConfirm : certFicate.getCertConfirmList()) {
				certConfirm.setCertId(certFicate.getCertId());
				if (null==certConfirm.getConfirmState()) {
					certConfirm.setConfirmState(StateMsg.noConfirm);
				}
				if (sendMsg) {
					/*发消息请求确认*/
					if (certConfirm.getConfirmState()!=StateMsg.originator) {
						User u = userMapper.findByName(certConfirm.getConfirmPhone());
						if (null!=u){
                            certConfirm.setUserId(u.getUserid());
							PushMsg pm = new PushMsg();
							pm.setCertFicateId(certFicate.getCertId().toString());
							pm.setTitle("存证待确认→");
							pm.setContent("您有一条【"+certFicate.getCertName()+"】的存证待确认");
							pm.setCertName(certFicate.getCertName());
							pm.setServerTime(DateUtil.timeToString2(new Date()));
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
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public CertFicate details(Integer certFicateId) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userMapper.findByName(username);
			CertFicate certFicate = certFicateMapper.selectByPrimaryKey(certFicateId);
			List<CertConfirm> list = certConfirmMapper.selectByCertId(certFicateId);
			certFicate.setCertConfirmList(list);
			if (list.size()>0){
				list.forEach(cc->{
					if (cc.getUserId()==user.getUserid()&&cc.getConfirmState()==1){
						certFicate.setCertIsconf(1);//1待自己确认
						return;
					}
					if (cc.getUserId()!=user.getUserid()&&cc.getConfirmState()==1){
						certFicate.setCertIsconf(2);//1待他人确认
						return;
					}
				});
			}
			if(certFicate.getCertFileIsSave().equals("1")){
				if (StringUtils.isNotBlank(certFicate.getCertFilesid())){
					String[] filesId = certFicate.getCertFilesid().split(",");
					List<CertFiles> certFiles = certFilesMapper.findByFilesIds(filesId);
					certFicate.setCertFilesList(certFiles);
				}
			}
			return certFicate;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public PageInfo<CertFicate> list(Page<CertFicate>page,String state) throws Exception {
		try {
			List<CertFicate>list = null;
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userMapper.findByName(username);
			Map<String,Object>map = new HashMap<>();
			Integer [] ids = null;
			List<CertConfirm> confirmList = null;
			if (StringUtils.isBlank(state)){
				confirmList = certConfirmMapper.selectByUserId(user.getUserid());
			}else if(state.equals("6")){
			    //待自己确认
				confirmList = certConfirmMapper.selectByuserIdAndState(user.getUserid().toString(),"1");
			}
			if (confirmList!=null&&confirmList.size()>0){
				ids = new Integer[confirmList.size()];
				for (int i = 0; i < confirmList.size(); i++) {
					ids[i]=confirmList.get(i).getCertId();
				}
			}
			map.put("certid",ids);
			map.put("state", "null".equals(state)?"":state);
			map.put("userId",user.getUserid());
			PageHelper.startPage(page.getPageNum(), StateMsg.pageSize);
			if (state.equals("6")){
				if (ids==null) return null;
				list = certFicateMapper.selectByIDs(ids);
			}else{
				list = certFicateMapper.list(map);
                if (list.size()>0){
					Iterator<CertFicate> iterator = list.iterator();
					while (iterator.hasNext()){
						CertFicate l = iterator.next();
						if (l.getCertStatus()==4){
							//待他人确认和待自己确认
							List<CertConfirm> certConfirms = certConfirmMapper.selectByCertId(l.getCertId());
							if (certConfirms.size()>0){
								certConfirms.forEach(cc->{
									if (cc.getUserId()==user.getUserid()&&cc.getConfirmState()==1){
										l.setCertIsconf(1);//1待自己确认
										return;
									}
									if (cc.getUserId()!=user.getUserid()&&cc.getConfirmState()==1){
										l.setCertIsconf(2);//1待他人确认
										return;
									}
								});
							}
						}else if(l.getCertStatus()==6) {
						    /*撤销的存证 确认人方不显示该存证*/
							List<CertConfirm> certConfirms = certConfirmMapper.selectByCertId(l.getCertId());
							if (certConfirms.size()>0){
								for (CertConfirm cc : certConfirms) {
									if (cc.getUserId()==user.getUserid()&&cc.getConfirmState()!=4){
										iterator.remove();
										break;
									}
								}
							}
						}
					}
                }
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

    @Override
    public ByteArrayResource getCertImg(Integer certId) throws Exception {
	    try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            CertFicate certFicate = certFicateMapper.selectByPrimaryKey(certId);
            certFicate.setCertOwner(username);
			String os = System.getProperty("os.name").toLowerCase();
			String qrCodePath = "";String templatePath = "";
			String s = UUID.randomUUID().toString();
			if(!os.startsWith("win")){
				qrCodePath = "/opt/czt-upload/"+s+".png";
				templatePath = "/opt/czt-upload/certTemplate/ct.png";
			}else{
				qrCodePath = "D:\\upload\\"+s+".png";
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
	public CertFicate temSave(TemCertFile temCertFile) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userMapper.findByName(username);
            CertFicate certFicate = null;
			if(null!=temCertFile.getTemFile()&&null!=temCertFile.getTemFile().getCertId()){
				TemFile temFile = temFileMapper.selectByCertId(temCertFile.getTemFile().getCertId().toString());
				temCertFile.getTemFile().setTemId(temFile.getTemId());
				temFileMapper.updateByPrimaryKeySelective(temCertFile.getTemFile());
				certFicate = certFicateMapper.selectByPrimaryKey(temCertFile.getTemFile().getCertId());
			}else{
				certFicate = new CertFicate();
				TemFile temFile = new TemFile();
				certFicate.setCertOwner(user.getUserid().toString());
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
}
