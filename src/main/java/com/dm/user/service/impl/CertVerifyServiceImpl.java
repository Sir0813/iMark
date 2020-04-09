package com.dm.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dm.cid.sdk.service.CIDService;
import com.dm.fchain.sdk.helper.CryptoHelper;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.CertFiles;
import com.dm.user.mapper.CertVerifyMapper;
import com.dm.user.service.CertFicateService;
import com.dm.user.service.CertFilesService;
import com.dm.user.service.CertVerifyService;
import com.dm.user.util.ShaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CertVerifyServiceImpl implements CertVerifyService {

    @Autowired
    private CertVerifyMapper certVerifyMapper;

    @Autowired
    private CertFilesService certFilesService;

    @Autowired
    private CertFicateService certFicateService;

    @Autowired
    private CIDService cidService;

    @Override
    public boolean verifyCert(CertFicate certFicate) throws Exception {
        CertFicate cf = certFicateService.selectByIdAndState(certFicate.getCertId());
        String[] filesId = certFicate.getCertFilesid().split(",");
        List<CertFiles> certFiles = certFilesService.findByFilesIds(filesId);
        if (null == cf) {
            deleteCertFile(certFiles);
            return false;
        }
        String[] ids = cf.getCertFilesid().split(",");
        List<CertFiles> fileList = certFilesService.findByFilesIds(ids);
        StringBuilder fhash = new StringBuilder();
        String chash = "";
        for (CertFiles files : fileList) {
            fhash.append(files.getFileHash() + ",");
        }
        for (CertFiles files : certFiles) {
            chash = ShaUtil.getMD5(files.getFilePath());
            if (!fhash.toString().contains(chash)) {
                deleteCertFile(certFiles);
                return false;
            }
        }
        deleteCertFile(certFiles);
        String realHash = CryptoHelper.hash(cf.getCertHash() + cf.getCertId());
        String result = cidService.query(realHash);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if ("200".equals(jsonObject.get("code").toString()) && "Success".equals(jsonObject.get("msg").toString())) {
            return true;
        }
        return false;
    }

    private void deleteCertFile(List<CertFiles> certFiles) throws Exception {
        for (int i = 0; i < certFiles.size(); i++) {
            CertFiles c = certFiles.get(i);
            File file = new File(c.getFilePath());
            file.delete();
            certFilesService.deleteByPrimaryKey(c.getFileId());
        }
    }
}
