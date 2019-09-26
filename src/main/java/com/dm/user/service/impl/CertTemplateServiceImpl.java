package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertTemplate;
import com.dm.user.entity.TemFile;
import com.dm.user.mapper.CertTemplateMapper;
import com.dm.user.mapper.TemFileMapper;
import com.dm.user.service.CertTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CertTemplateServiceImpl implements CertTemplateService {

    @Autowired
    private CertTemplateMapper certTemplateMapper;

    @Autowired
    private TemFileMapper temFileMapper;

    @Override
    public Result list() throws Exception {
        try {
            List<CertTemplate> templateList = certTemplateMapper.list();
            return ResultUtil.success(templateList);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public CertTemplate getByTemplateType(String type) throws Exception {
        try {
            CertTemplate ct = certTemplateMapper.getByTemplateType(type);
            return ct;
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    public String fileEdit(String certId) throws Exception {
        try {
            TemFile tf = temFileMapper.selectByCertId(certId);
            return tf.getTemFileText();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
