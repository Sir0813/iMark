package com.dm.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.exception.GlobalException;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertFicateMapper;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.mapper.CertVerifyMapper;
import com.dm.user.service.CertVerifyService;
import com.dm.user.util.ShaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CertVerifyServiceImpl  implements CertVerifyService {

    @Autowired
    private CertVerifyMapper certVerifyMapper;

    @Autowired
    private CertFilesMapper certFilesMapper;

    @Autowired
    private CertFicateMapper certFicateMapper;

    @Autowired
    private CIDService cidService;

    @Override
    public boolean verifyCert(CertFicate certFicate) throws Exception {
        try {
            CertFicate cf = certFicateMapper.selectByIdAndState(certFicate.getCertId());
            if (null==cf) return false;
            String[] ids = cf.getCertFilesid().split(",");
            List<CertFiles> fileList = certFilesMapper.findByFilesIds(ids);
            String[] filesId = certFicate.getCertFilesid().split(",");
            List<CertFiles> certFiles = certFilesMapper.findByFilesIds(filesId);
            String fhash = "";String cfHash = "";String result = "";
            for (CertFiles files : fileList) {
                fhash+=(files.getFileHash()+",");
            }
            for (CertFiles files : certFiles) {
                String str = CryptoHelper.hash(ShaUtil.getFileByte(files.getFilePath()));
                if (!fhash.contains(str)){
                    return false;
                }else{
                    cfHash+=str;
                }
            }
            String replace = fhash.replace(",", "");
            if (fileList.size()==1){
                if (!cf.getCertHash().equals(cfHash)) return false;
                result = cidService.query(cfHash);
            } else {
                String hashs = CryptoHelper.hash(replace);
                if (!cf.getCertHash().equals(hashs)) return false;
                result = cidService.query(hashs);
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.get("code").toString().equals("200")
            &&jsonObject.get("msg").toString().equals("Success"))
            return true;
            return false;
        } catch (GlobalException e) {
            throw new Exception(e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
