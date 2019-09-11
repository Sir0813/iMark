package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFicate;
import com.dm.user.service.CertVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class CertVerifyController extends BaseController {

    @Autowired
    private CertVerifyService certVerifyService;

    /**
     * 验证
     * @param certFicate
     * @return
     * @throws Exception
     */
    @RequestMapping("/cert")
    public Result verifyCert(@RequestBody CertFicate certFicate) throws Exception{
        boolean b = certVerifyService.verifyCert(certFicate);
        return ResultUtil.success(b);
    }

}
