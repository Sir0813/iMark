package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.*;
import com.dm.user.mapper.*;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.OutCertService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class OutCertServiceImpl implements OutCertService {

    @Autowired
    private OutCertMapper outCertMapper;

    @Autowired
    private CertFicateMapper certFicateMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CertFilesMapper certFilesMapper;

    @Override
    public String downloadOutCertTemplate(String certIds) throws Exception {
        String[] split = certIds.split(",");
        List<CertFicate> certFicates = certFicateMapper.selectByCertIDs(split);
        List<Map<String,String>> newlist = new ArrayList<>();
        for (int i = 0; i < certFicates.size(); i++) {
            CertFicate certFicate =  certFicates.get(i);
            Map<String,String> dataMap = new HashMap<>();
            dataMap.put("name", certFicate.getCertName());
            dataMap.put("time", certFicate.getCertDate().toString());
            dataMap.put("certno", certFicate.getCertChainno());
            dataMap.put("address", certFicate.getCertAddress());
            dataMap.put("from", "iMark");
            newlist.add(dataMap);
        }
        Map<String, Object> dataMapss = new HashMap<>();
        dataMapss.put("listarray", newlist);
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("utf-8");
        //指定模板路径的第二种方式,我的路径是D:/      还有其他方式
        // 输出文档路径及名称
        String str = UUID.randomUUID().toString()+".doc";
        String osname = System.getProperty("os.name").toLowerCase();
        File outFile;
        String resultPath = "";
        if (osname.startsWith("win")){
            configuration.setDirectoryForTemplateLoading(new File("D:\\upload\\"));
            outFile = new File("D:\\upload\\"+str);
            resultPath = "http://192.168.3.101/img/"+str;
        }else{
            configuration.setDirectoryForTemplateLoading(new File("/opt/czt-upload/outcert/"));
            outFile = new File("/opt/czt-upload/outcert/"+str);
            resultPath = "http://114.244.37.10:7080/img/outcert/"+str;
        }
        //以utf-8的编码读取ftl文件
        Template t =  configuration.getTemplate("outcert.ftl","utf-8");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
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
                Contact contact =  contactList.get(i);
                User user = userMapper.findByName(contact.getContactPhone());
                if (null!=user){
                    contact.setUserId(user.getUserid());
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
            PageHelper.startPage(page.getPageNum(), StateMsg.pageSize);
            String userId = LoginUserHelper.getUserId();
            if ("mysend".equals(state)){
                list = outCertMapper.list(userId);
            }else if ("tome".equals(state)){
                List<Contact> contacts = contactMapper.selectByUserId(userId);
                if (contacts.size()>0){
                    for (int i = 0; i < contacts.size(); i++) {
                        Contact contact =  contacts.get(i);
                        OutCert outCert = outCertMapper.selectByPrimaryKey(contact.getOutCertId());
                        list.add(outCert);
                    }
                }
            }else{
                throw new Exception("显示异常");
            }
            if (list.size()==0) return null;
            PageInfo<OutCert> pageInfo = new PageInfo<>(list);
            if (page.getPageNum()>pageInfo.getPages()) return null;
            return pageInfo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public OutCert details(String outCertId) throws Exception {
        try {
            OutCert outCert = outCertMapper.selectByPrimaryKey(Integer.parseInt(outCertId));
            User user = userMapper.selectByPrimaryKey(outCert.getUserId());
            /*发起人*/
            outCert.setPromoter(user.getUsername());
            CertFiles certFiles = certFilesMapper.selectByPrimaryKey(outCert.getFileId());
            /*存证说明文件*/
            outCert.setOutCertExplain(certFiles.getFileUrl());
            String[] split = outCert.getCertId().split(",");
            List<CertFicate> certFicates = certFicateMapper.selectByCertIDs(split);
            String filesId = "";
            for (int i = 0; i < certFicates.size(); i++) {
                CertFicate certFicate =  certFicates.get(i);
                String certFilesid = certFicate.getCertFilesid();
                filesId+=certFilesid;
            }
            String id = distinctStringWithDot(filesId);
            List<CertFiles> list = certFilesMapper.findByFilesIds(id.split(","));
            for (int i = 0; i < list.size(); i++) {
                CertFiles files =  list.get(i);
                CertFicate certFicate = certFicateMapper.selectByPrimaryKey(files.getCertId());
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

    private static String distinctStringWithDot(String str) {
        String[]array=str.split(",");
        List<String> list = new ArrayList<>();
        for(int i=0;i<array.length;i++){
            for(int j=i+1;j<array.length;j++){
                if(array[i].equals(array[j])){
                    j = ++i;
                }
            }
            list.add(array[i]);
        }
        String newStr="";
        for(String s:list){
            newStr=newStr+s+",";
        }
        return newStr;
    }
}
