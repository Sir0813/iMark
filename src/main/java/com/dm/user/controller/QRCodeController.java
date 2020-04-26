package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.util.QRCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "二维码扫描")
@RestController
@RequestMapping(value = "/qrcode")
public class QRCodeController extends BaseController {

    @ApiOperation(value = "手机扫码", response = ByteArrayResource.class)
    @RequestMapping(value = "/scanQRCode/{code}", method = RequestMethod.GET)
    public Result scanQRCode(@PathVariable String code) throws Exception {
        QRCodeUtil.saveUserName(code);
        return ResultUtil.success();
    }

}
