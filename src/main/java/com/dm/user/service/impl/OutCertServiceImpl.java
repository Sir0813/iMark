package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.Contact;
import com.dm.user.entity.OutCert;
import com.dm.user.entity.User;
import com.dm.user.mapper.CertFicateMapper;
import com.dm.user.mapper.ContactMapper;
import com.dm.user.mapper.OutCertMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.OutCertService;
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
                    contact.setOutCertId(outCert.getOutCertId());
                    contactMapper.insertSelective(contact);
                }
            }
            outCertMapper.updateByPrimaryKeySelective(outCert);
        } catch (NumberFormatException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
