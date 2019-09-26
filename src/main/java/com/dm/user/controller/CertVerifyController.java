package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFicate;
import com.dm.user.service.CertVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cui
 * @date 2019-09-26
 */
@Api(description="验证")
@RestController
@RequestMapping("/verify")
public class CertVerifyController extends BaseController {

    @Autowired
    private CertVerifyService certVerifyService;

    @ApiOperation(value="存证校验", response= ResultUtil.class)
    @RequestMapping(value = "/cert" ,method = RequestMethod.POST)
    public Result verifyCert(@RequestBody CertFicate certFicate) throws Exception{
        boolean b = certVerifyService.verifyCert(certFicate);
        return ResultUtil.success(b);
    }

}
