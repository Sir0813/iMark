package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.AgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "服务协议")
@RestController
@RequestMapping(value = "/agreement")
public class AgreementController extends BaseController {

    @Autowired
    private AgreementService agreementService;

    @ApiOperation(value = "服务协议", response = ResultUtil.class)
    @RequestMapping(value = "/desc", method = RequestMethod.GET)
    public Result desc() throws Exception {
        return agreementService.desc();
    }

}
