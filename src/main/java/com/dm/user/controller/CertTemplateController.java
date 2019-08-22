package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertTemplate;
import com.dm.user.service.CertTemplateService;
import com.dm.user.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cert/template")
public class CertTemplateController extends BaseController {

    @Autowired
    private CertTemplateService certTemplateService;

    /**
     * 模板列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list" , method = RequestMethod.GET)
    public Result list() throws Exception {
        return certTemplateService.list();
    }

    /**
     * 编辑合同模板
     * @return
     */
    @RequestMapping(value="/edit", method = RequestMethod.GET)
    public Result getHtmlTemplate(String type) throws Exception {
        CertTemplate ct = certTemplateService.getByTemplateType(type);
        String htmlTemplate = FileUtil.getHtmlTemplate(ct.getTemplatePath()).replace("\t","");
        return ResultUtil.success(htmlTemplate);
    }

    /**
     * 编辑合同模板
     * @return
     */
    @RequestMapping(value="/fileEdit", method = RequestMethod.GET)
    public Result fileEdit(String certId) throws Exception {
        String htmlTemplate = certTemplateService.fileEdit(certId);
        return ResultUtil.success(htmlTemplate);
    }

}
