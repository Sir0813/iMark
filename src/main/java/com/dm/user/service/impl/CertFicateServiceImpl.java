package com.dm.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.CertFicateMapper;
import com.dm.user.mapper.TemFileMapper;
import com.dm.user.msg.CertStateEnum;
import com.dm.user.msg.CertTypeEnum;
import com.dm.user.msg.ConfirmEnum;
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
@Transactional(rollbackFor = Exception.class)
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
            JSONObject js = new JSONObject();
            certFicate.setCertOwner(LoginUserHelper.getUserId());
            certFicate.setCertIsDelete(StateMsg.CERT_NOT_DELETE);
            List<CertFiles> certFiles = null;
            // 模板存证
            if (certFicate.getCertType() == CertTypeEnum.TEMPLATE.getCode()) {
                if (certFicate.getCertStatus() == CertStateEnum.TO_CERT.getCode()) {
                    TemFile temFile = certFicateService.selectByCertId(certFicate.getCertId().toString());
                    Integer fileId = pdfConvertUtil.acceptPage(temFile.getTemFileText(), temFile.getCertId());
                    certFicate.setCertFilesid(fileId.toString());
                    // 生成文件hash
                    certFiles = getHash(js, certFicate);
                } else {
                    certFiles = new ArrayList<>();
                }
            } else {
                certFiles = getHash(js, certFicate);
            }
            if (null == certFiles) {
                return null;
            }
            certFicate.setCertPostDate(new Date());
            if (null != certFicate.getCertId()) {
                certFicateMapper.updateByPrimaryKeySelective(certFicate);
            } else {
                certFicateMapper.insertSelective(certFicate);
            }
            // 证书编号
            DecimalFormat df = new DecimalFormat("000000");
            String id = df.format(certFicate.getCertId());
            certFicate.setCertCode("DMS01" + id);
            // 存证入链
            if (CertStateEnum.TO_CERT.getCode() == certFicate.getCertStatus()) {
                // 对接存证sdk
                String dataHash = CryptoHelper.hash(certFicate.getCertHash() + certFicate.getCertId());
                js.put("文件摘要", certFicate.getCertHash());
                js.put("存证位置", certFicate.getCertAddress());
                js.put("证书编号", certFicate.getCertCode());
                js.put("文件签名", certFicate.getSignature());
                Result result = cidService.save(dataHash, certFicate.getCertName(), DateUtil.timeToString2(new Date()), js.toString());
                if (result.getData() instanceof TransactionResult) {
                    TransactionResult tr = (TransactionResult) result.getData();
                    certFicate.setCertChainno(tr.getTransactionID());
                    certFicate.setCertBlockNumber(String.valueOf(tr.getBlockNumber()));
                } else {
                    throw new Exception(result.getMsg());
                }
                certFicate.setCertDate(new Date());
                certFicate.setCertStatus(certFicate.getCertIsconf() == 1 ?
                        CertStateEnum.OTHERS_CONFIRM.getCode() : CertStateEnum.CERT_SUCCESS.getCode());
                sendMsg = true;
            } else {
                certFicate.setCertStatus(CertStateEnum.NO_CERT.getCode());
            }
            certFicateMapper.updateByPrimaryKeySelective(certFicate);
            // 添加发起人 推送存证消息
            if (certFicate.getCertIsconf() == 1) {
                certIsConfirm(LoginUserHelper.getUserName(), sendMsg, certFicate, Integer.parseInt(LoginUserHelper.getUserId()));
            }
            if (null != certFiles) {
                for (int i = 0; i < certFiles.size(); i++) {
                    CertFiles cf = certFiles.get(i);
                    cf.setCertId(certFicate.getCertId());
                    certFilesService.updateByPrimaryKeySelective(cf);
                    // 文件不保存至证云链 将文件删除
                    if (StateMsg.CERT_FILE_IS_DELETE.equals(certFicate.getCertFileIsSave())) {
                        File file = new File(cf.getFilePath());
                        file.delete();
                        certFilesService.deleteByCertId(certFicate.getCertId());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return certFicate;
    }

    /**
     * 生成文件hash
     *
     * @param certFicate
     * @return
     * @throws Exception
     */
    private List<CertFiles> getHash(JSONObject js, CertFicate certFicate) throws Exception {
        try {
            if (StringUtils.isBlank(certFicate.getCertFilesid())) {
                return null;
            }
            String[] filesId = certFicate.getCertFilesid().split(",");
            List<CertFiles> certFiles = certFilesService.findByFilesIds(filesId);
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < certFiles.size(); i++) {
                CertFiles certFile = certFiles.get(i);
                hash.append(certFile.getFileHash());
                js.put(certFile.getFileName(), certFile.getFileHash());
            }
            String dataHash = CryptoHelper.hash(hash.toString());
            certFicate.setCertHash(dataHash);
            return certFiles;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 添加确认人信息 发送推送消息
     *
     * @param username   用户名称
     * @param sendMsg    是否发送通知
     * @param certFicate 存证实例
     * @param userId     用户ID
     * @throws Exception 异常信息
     */
    private void certIsConfirm(String username, boolean sendMsg, CertFicate certFicate, Integer userId) throws Exception {
        try {
            /*添加发起人*/
            if (null != certFicate.getCertId()) {
                certConfirmService.deleteByCertId(certFicate.getCertId());
            }
            CertConfirm cc = new CertConfirm();
            cc.setConfirmDate(new Date());
            cc.setConfirmState(ConfirmEnum.ORIGINATOR.getCode());
            cc.setConfirmPhone(username);
            cc.setConfirmPerson(username);
            cc.setUserId(userId);
            certFicate.getCertConfirmList().add(cc);
            for (CertConfirm certConfirm : certFicate.getCertConfirmList()) {
                certConfirm.setCertId(certFicate.getCertId());
                if (null == certConfirm.getConfirmState()) {
                    certConfirm.setConfirmState(ConfirmEnum.NO_CONFIRM.getCode());
                }
                User u = userService.findByName(certConfirm.getConfirmPhone());
                if (null != u) {
                    certConfirm.setUserId(u.getUserid());
                }
                if (sendMsg) {
                    /*发消息请求确认*/
                    if (certConfirm.getConfirmState() != ConfirmEnum.ORIGINATOR.getCode()) {
                        PushMsg pm = new PushMsg();
                        pm.setCertFicateId(certFicate.getCertId().toString());
                        pm.setTitle(StateMsg.SAVE_CERT_TITLE);
                        pm.setContent(StateMsg.SAVE_CERT_CONTENT.replace("certName", certFicate.getCertName()));
                        pm.setCertName(certFicate.getCertName());
                        pm.setServerTime(DateUtil.timeToString2(new Date()));
                        pm.setType("1");
                        pm.setState("0");
                        pm.setIsRead("0");
                        pm.setReceive(certConfirm.getConfirmPhone());
                        pushMsgService.insertSelective(pm);
                        if (null != u) {
                            pm.setUserId(u.getUserid());
                            String json = new Gson().toJson(pm);
                            String aliases = HttpSendUtil.getData("aliases", u.getUsername());
                            JSONObject jsonObject = JSONObject.parseObject(aliases);
                            String registrationIds = jsonObject.get("registration_ids").toString();
                            if (!"[]".equals(registrationIds)) {
                                int resout = PushUtil.getInstance().sendToRegistrationId(u.getUsername(), pm.getTitle(), json);
                                if (1 == resout) {
                                    pm.setState("1");
                                }
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
            if (list.size() > 0) {
                list.forEach(cc -> {
                    if (null != cc.getUserId()) {
                        if (cc.getUserId() == Integer.parseInt(LoginUserHelper.getUserId()) &&
                                cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                            certFicate.setCertIsconf(1);//1待自己确认 仅前端判断用
                            return;
                        }
                        if (cc.getUserId() != Integer.parseInt(LoginUserHelper.getUserId()) &&
                                cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                            certFicate.setCertIsconf(2);//2待他人确认 仅前端判断用
                            return;
                        }
                    } else {
                        if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                            certFicate.setCertIsconf(1);
                            return;
                        }
                        if (!cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                            certFicate.setCertIsconf(2);
                            return;
                        }
                    }
                });
            }
            if ("1".equals(certFicate.getCertFileIsSave())) {
                if (StringUtils.isNotBlank(certFicate.getCertFilesid())) {
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
    public PageInfo<CertFicate> listCerts(Page<CertFicate> page, String state, String certName) throws Exception {
        try {
            List<CertFicate> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>(16);
            Integer[] ids = null;
            List<CertConfirm> confirmList = null;
            if (StringUtils.isBlank(state)) {
                confirmList = certConfirmService.selectByUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            } else if (StateMsg.CONFIRM_TO_ME.equals(state)) {
                // 待自己确认
                confirmList = certConfirmService.selectByuserIdAndState(LoginUserHelper.getUserId(), "1");
            }
            if (confirmList != null && confirmList.size() > 0) {
                ids = new Integer[confirmList.size()];
                for (int i = 0; i < confirmList.size(); i++) {
                    ids[i] = confirmList.get(i).getCertId();
                }
            }
            if (StringUtils.isBlank(certName)) {
                map.put("certid", ids);
            }
            map.put("state", "null".equals(state) ? "" : state);
            map.put("userId", LoginUserHelper.getUserId());
            map.put("certName", certName);
            // 待自己确认
            if (state != null && StateMsg.CONFIRM_TO_ME.equals(state)) {
                if (ids == null) {
                    return null;
                }
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                map.put("notDelete", Integer.valueOf(StateMsg.CERT_FILE_NOT_DELETE));
                map.put("otherConfirm", CertStateEnum.OTHERS_CONFIRM.getCode());
                list = certFicateMapper.selectByIDs(map);
            } else {
                PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
                map.put("fileNotDelete", Integer.valueOf(StateMsg.CERT_FILE_NOT_DELETE));
                map.put("isRevoke", CertStateEnum.IS_REVOKE.getCode());
                list = certFicateMapper.list(map);
                certConfirmState(list, state);
            }
            if (list.size() == 0) {
                return null;
            }
            PageInfo<CertFicate> pageInfo = new PageInfo<>(list);
            if (page.getPageNum() > pageInfo.getPages()) {
                return null;
            }
            return pageInfo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private void certConfirmState(List<CertFicate> list, String state) throws Exception {
        if (list.size() > 0) {
            Iterator<CertFicate> iterator = list.iterator();
            while (iterator.hasNext()) {
                CertFicate l = iterator.next();
                if (l.getCertStatus().equals(CertStateEnum.OTHERS_CONFIRM.getCode())) {
                    //待他人确认和待自己确认
                    List<CertConfirm> certConfirms = certConfirmService.selectByCertId(l.getCertId());
                    if (certConfirms.size() > 0) {
                        certConfirms.forEach(cc -> {
                            if (null != cc.getUserId()) {
                                // 用户确认人添加自己去除重复
                                if (StringUtils.isNotBlank(state)) {
                                    if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                            cc.getUserId() == Integer.parseInt(LoginUserHelper.getUserId()) &&
                                            cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                                        iterator.remove();
                                        return;
                                    }
                                }
                                if (cc.getUserId() == Integer.parseInt(LoginUserHelper.getUserId()) &&
                                        cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                                    l.setCertIsconf(1);//1待自己确认 仅前端判断用
                                    return;
                                }
                                if (cc.getUserId() != Integer.parseInt(LoginUserHelper.getUserId()) &&
                                        cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                                    l.setCertIsconf(2);//1待他人确认 仅前端判断用
                                    return;
                                }
                            } else {
                                if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                        cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                                    l.setCertIsconf(1);
                                    return;
                                }
                                if (!cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                        cc.getConfirmState() == ConfirmEnum.NO_CONFIRM.getCode()) {
                                    l.setCertIsconf(2);
                                    return;
                                }
                            }
                        });
                    }
                } else if (l.getCertStatus().equals(Integer.valueOf(StateMsg.CONFIRM_TO_ME))) {
                    // 撤销的存证 确认人方不显示该存证
                    List<CertConfirm> certConfirms = certConfirmService.selectByCertId(l.getCertId());
                    if (certConfirms.size() > 0) {
                        for (CertConfirm cc : certConfirms) {
                            if (null != cc.getUserId()) {
                                if (cc.getUserId() == Integer.parseInt(LoginUserHelper.getUserId()) &&
                                        cc.getConfirmState() != ConfirmEnum.ORIGINATOR.getCode() &&
                                        cc.getConfirmState() != ConfirmEnum.REVOKE_CONFIRM.getCode()) {
                                    iterator.remove();
                                    break;
                                }
                            } else {
                                if (cc.getConfirmPhone().equals(LoginUserHelper.getUserName()) &&
                                        cc.getConfirmState() != ConfirmEnum.ORIGINATOR.getCode()) {
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
            certFicateMapper.updateCertRevoke(CertStateEnum.IS_REVOKE.getCode(), Integer.parseInt(certId));
            pushMsgService.deleteByCertId(certId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void draftDel(CertFicate certFicate) throws Exception {
        try {
            //certFicate.setCertDelDate(new Date());
            //certFicate.setCertIsDelete(StateMsg.CERT_IS_DELETE);
            certFicateMapper.deleteByPrimaryKey(certFicate.getCertId());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void returnReason(Map<String, Object> map) throws Exception {
        try {
            if (null == map.get("reason") || StringUtils.isBlank(map.get("reason").toString())) {
                throw new Exception(StateMsg.REASONMSG);
            }
            map.put("confirmPhone", LoginUserHelper.getUserName());
            certConfirmService.updateByCertId(map);
            certFicateMapper.updateReasonByCertId(CertStateEnum.IS_RETURN.getCode(), Integer.parseInt(map.get("certId").toString()));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void confirm(Map<String, Object> map) throws Exception {
        try {
            map.put("userId", LoginUserHelper.getUserId());
            certConfirmService.updateConfirmState(map);
            List<CertConfirm> confirmList = certConfirmService.selectByState(map);
            if (confirmList.size() == 0) {
                map.put("certSuccess", CertStateEnum.CERT_SUCCESS.getCode());
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
            String fileName = UUID.randomUUID().toString() + fileSuffix;
            if (!os.startsWith(StateMsg.OS_NAME)) {
                qrCodePath = "/opt/czt-upload/" + fileName;
                templatePath = "/opt/czt-upload/certTemplate/ct.png";
            } else {
                qrCodePath = "D:\\upload\\" + fileName;
                templatePath = "D:\\ct.png";
            }
            QRCodeGenerator.generateQRCodeImage(certFicate.getCertCode(), qrCodePath);
            ByteArrayResource mark = CertImgUtil.createStringMark(certFicate, templatePath, qrCodePath);
            return mark;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public CertFicate saveTemplate(TemCertFile temCertFile) throws Exception {
        try {
            CertFicate certFicate = null;
            if (null != temCertFile.getTemFile() && null != temCertFile.getTemFile().getCertId()) {
                TemFile temFile = temFileMapper.selectByCertId(temCertFile.getTemFile().getCertId().toString());
                temCertFile.getTemFile().setTemId(temFile.getTemId());
                temFileMapper.updateByPrimaryKeySelective(temCertFile.getTemFile());
                certFicate = certFicateMapper.selectByPrimaryKey(temCertFile.getTemFile().getCertId());
            } else {
                certFicate = new CertFicate();
                TemFile temFile = new TemFile();
                certFicate.setCertOwner(LoginUserHelper.getUserId());
                certFicate.setCertFileIsSave(StateMsg.CERT_FILE_NOT_DELETE);
                certFicate.setCertIsDelete(StateMsg.CERT_NOT_DELETE);
                certFicate.setCertStatus(CertStateEnum.NO_CERT.getCode());
                certFicate.setCertName("模板存证");
                certFicate.setCertType(CertTypeEnum.TEMPLATE.getCode());
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
            return certFicateMapper.selectByIdAndState(CertStateEnum.CERT_SUCCESS.getCode(), Integer.valueOf(StateMsg.CERT_FILE_NOT_DELETE), certId);
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
