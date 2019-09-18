package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.OutCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/out")
public class OutCertController extends BaseController {

    @Autowired
    private OutCertService outCertService;

    @RequestMapping(value = "/cert/template", method = RequestMethod.GET)
    public Result downloadOutCertTemplate(String certIds) throws Exception {
        String downloadPath = outCertService.downloadOutCertTemplate (certIds);
        return ResultUtil.success(downloadPath);
    }

}
