package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.ContactMapper;
import com.dm.user.mapper.OutCertMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.*;
import com.dm.user.util.FileUtil;
import com.dm.user.util.PushUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OutCertServiceImpl implements OutCertService {

    @Autowired
    private OutCertMapper outCertMapper;

    @Autowired
    private CertFicateService certFicateService;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CertFilesService certFilesService;

    @Autowired
    private PushMsgService pushMsgService;

    @Autowired
    private FileUtil fileUtil;

    @Value("${upload.baseUrl}")
    private String baseUrl;

    @Override
    public String downloadOutCertTemplate(String certIds) throws Exception {
        String[] split = certIds.split(",");
        List<CertFicate> certFicates = certFicateService.selectByCertIDs(split);
        List<Map<String, String>> newlist = new ArrayList<>();
        for (int i = 0; i < certFicates.size(); i++) {
            CertFicate certFicate = certFicates.get(i);
            Map<String, String> dataMap = new HashMap<>(16);
            dataMap.put("name", certFicate.getCertName());
            dataMap.put("time", certFicate.getCertDate().toString());
            dataMap.put("certno", certFicate.getCertChainno());
            dataMap.put("address", certFicate.getCertAddress());
            dataMap.put("from", "iMark");
            newlist.add(dataMap);
        }
        Map<String, Object> dataMapss = new HashMap<>(16);
        dataMapss.put("listarray", newlist);
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("utf-8");
        String fileSuffix = ".doc";
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        String osname = System.getProperty("os.name").toLowerCase();
        File outFile;
        String resultPath = "";
        if (osname.startsWith(StateMsg.OS_NAME)) {
            configuration.setDirectoryForTemplateLoading(new File("D:\\upload\\"));
            outFile = new File("D:\\upload\\" + fileName);
            resultPath = "http://192.168.3.101/img/" + fileName;
        } else {
            configuration.setDirectoryForTemplateLoading(new File("/opt/czt-upload/outcert/"));
            outFile = new File("/opt/czt-upload/outcert/" + fileName);
            resultPath = baseUrl + "dms/outcert/" + fileName;
        }
        //以utf-8的编码读取ftl文件
        Template t = configuration.getTemplate("outcert.ftl", "utf-8");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);
        t.process(dataMapss, out);
        out.close();
        return resultPath;
    }

    @Override
    public void save(OutCert outCert) throws Exception {
        try {
            outCert.setOutCertTime(new Date());
            outCert.setUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            outCertMapper.insertSelective(outCert);
            List<Contact> contactList = outCert.getContactList();
            for (int i = 0; i < contactList.size(); i++) {
                Contact contact = contactList.get(i);
                AppUser user = userService.findByName(contact.getContactPhone());
                PushMsg pm = new PushMsg();
                pm.setTitle(StateMsg.OUT_CERT_TITLE);
                pm.setContent(StateMsg.OUT_CERT_CONTENT.replace("outCertName", outCert.getOutCertName()));
                pm.setCertName(outCert.getOutCertName());
                pm.setServerTime(DateUtil.getSystemTimeStr());
                pm.setType("2");
                pm.setState("0");
                pm.setIsRead("0");
                pm.setReceive(contact.getContactPhone());
                pm.setCertFicateId(String.valueOf(outCert.getOutCertId()));
                pushMsgService.insertSelective(pm);
                if (null != user) {
                    contact.setUserId(user.getUserid());
                    /*出证通知*/
                    pm.setUserId(user.getUserid());
                    String json = new Gson().toJson(pm);
                    int resout = PushUtil.getInstance().sendToRegistrationId(contact.getContactPhone(), pm.getTitle(), json);
                    if (resout == 1) {
                        pm.setState("1");
                    }
                    pushMsgService.updateByPrimaryKeySelective(pm);
                }
                contact.setOutCertId(outCert.getOutCertId());
                contactMapper.insertSelective(contact);
            }
            outCertMapper.updateByPrimaryKeySelective(outCert);
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public PageInfo<OutCert> list(Page<OutCert> page, String state) throws Exception {
        try {
            List<OutCert> list = new ArrayList<>();
            PageHelper.startPage(page.getPageNum(), StateMsg.PAGE_SIZE);
            String userId = LoginUserHelper.getUserId();
            String mySend = "mysend";
            String toMe = "tome";
            if (mySend.equals(state)) {
                list = outCertMapper.list(userId);
            } else if (toMe.equals(state)) {
                List<Contact> contacts = contactMapper.selectByUserId(userId);
                if (contacts.size() > 0) {
                    for (int i = 0; i < contacts.size(); i++) {
                        Contact contact = contacts.get(i);
                        OutCert outCert = outCertMapper.selectByPrimaryKey(contact.getOutCertId());
                        list.add(outCert);
                    }
                }
            } else {
                return null;
            }
            if (list.size() == 0) {
                return null;
            }
            PageInfo<OutCert> pageInfo = new PageInfo<>(list);
            if (page.getPageNum() > pageInfo.getPages()) {
                return null;
            }
            return pageInfo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public OutCert details(String outCertId) throws Exception {
        try {
            OutCert outCert = outCertMapper.selectByPrimaryKey(Integer.parseInt(outCertId));
            AppUser user = userService.selectByPrimaryKey(outCert.getUserId());
            /*发起人*/
            outCert.setPromoter(user.getUsername());
            CertFiles certFiles = certFilesService.selectByPrimaryKey(outCert.getFileId());
            /*存证说明文件*/
            outCert.setOutCertExplain(certFiles.getFileUrl());
            String[] split = outCert.getCertId().split(",");
            List<CertFicate> certFicates = certFicateService.selectByCertIDs(split);
            String filesId = "";
            for (int i = 0; i < certFicates.size(); i++) {
                CertFicate certFicate = certFicates.get(i);
                String certFilesid = certFicate.getCertFilesid();
                filesId += certFilesid;
            }
            String id = distinctStringWithDot(filesId);
            List<CertFiles> list = certFilesService.findByFilesIds(id.split(","));
            for (int i = 0; i < list.size(); i++) {
                CertFiles files = list.get(i);
                CertFicate certFicate = certFicateService.selectByPrimaryKey(files.getCertId());
                files.setFileHash(certFicate.getCertName());
            }
            outCert.setList(list);
            return outCert;
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public String downZip(String outCertId) throws Exception {
        try {
            List<File> fileList = new ArrayList<>();
            OutCert outCert = outCertMapper.selectByPrimaryKey(Integer.parseInt(outCertId));
            /*发起人*/
            CertFiles certFiles = certFilesService.selectByPrimaryKey(outCert.getFileId());
            /*出证说明文件*/
            String filePath = certFiles.getFilePath();
            fileList.add(new File(filePath));
            String[] split = outCert.getCertId().split(",");
            List<CertFicate> certFicates = certFicateService.selectByCertIDs(split);
            String filesId = "";
            for (int i = 0; i < certFicates.size(); i++) {
                CertFicate certFicate = certFicates.get(i);
                String certFilesid = certFicate.getCertFilesid();
                filesId += certFilesid;
            }
            String id = distinctStringWithDot(filesId);
            List<CertFiles> list = certFilesService.findByFilesIds(id.split(","));
            for (int i = 0; i < list.size(); i++) {
                CertFiles files = list.get(i);
                fileList.add(new File(files.getFilePath()));
            }
            String osname = System.getProperty("os.name").toLowerCase();
            String zipFilePath = "";
            String downloadPath = "";
            String path = UUID.randomUUID().toString();
            if (osname.startsWith(StateMsg.OS_NAME)) {
                zipFilePath = "D:\\upload";
                downloadPath = "http://192.168.3.101/img/" + path + ".zip";
            } else {
                zipFilePath = "/opt/czt-upload/outcert/zip";
                downloadPath = baseUrl + "dms/outcert/zip/" + path + ".zip";
            }
            boolean b = fileUtil.fileToZip(fileList, zipFilePath, path);
            if (!b) {
                throw new Exception("压缩失败");
            }
            return downloadPath;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static String distinctStringWithDot(String str) {
        String[] array = str.split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i].equals(array[j])) {
                    j = ++i;
                }
            }
            list.add(array[i]);
        }
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s + ",");
        }
        return sb.toString();
    }
}
