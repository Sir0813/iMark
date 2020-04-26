package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.AppUser;
import com.dm.user.service.CertFicateService;
import com.dm.user.service.UserService;
import com.dm.user.util.QRCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(description = "公共调用方法")
@RestController
@RequestMapping(value = "/api")
public class PublicController extends BaseController {

    @Autowired
    private CertFicateService certFicateService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "发送验证码", response = ResultUtil.class)
    @RequestMapping(value = "/sendVeriCode/{phone}/{type}", method = RequestMethod.GET)
    public Result sendVeriCode(@PathVariable String phone, @PathVariable String type) throws Exception {
        return userService.sendVeriCode(phone, type);
    }

    @ApiOperation(value = "用户注册", response = ResultUtil.class)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@Valid @RequestBody AppUser user) throws Exception {
        return userService.userRegister(user);
    }

    @ApiOperation(value = "短信验证码登录", response = ResultUtil.class)
    @RequestMapping(value = "/dynamic/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "动态验证码", dataType = "String")
    })
    public Result dynamicLogin(@RequestBody Map<String, Object> map) throws Exception {
        return userService.dynamicLogin(map);
    }

    @ApiOperation(value = "找回密码 下一步", response = ResultUtil.class)
    @RequestMapping(value = "/nextOperate", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "veriCode", value = "验证码", dataType = "String")
    })
    public Result nextOperate(@RequestBody Map<String, Object> map) throws Exception {
        return userService.nextOperate(map);
    }

    @ApiOperation(value = "找回密码", response = ResultUtil.class)
    @RequestMapping(value = "/retrievePwd", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String")
    })
    public Result retrievePwd(@RequestBody Map<String, Object> map) throws Exception {
        return userService.retrievePwd(map);
    }

    @ApiOperation(value = "获取存证证书", response = ByteArrayResource.class)
    @RequestMapping(value = "/getCertImg/{certCode}", method = RequestMethod.GET, headers = "Accept=image/jpeg")
    public ByteArrayResource getCertImg(@PathVariable String certCode) throws Exception {
        ByteArrayResource bar = certFicateService.getPubCertImg(certCode);
        return bar;
    }

    @ApiOperation(value = "二维码登录", response = Result.class)
    @RequestMapping(value = "/l/{QRCode}", method = RequestMethod.GET)
    public Result qrCodeLogin(@PathVariable String QRCode) throws Exception {
        return QRCodeUtil.getUserName(QRCode);
    }


}
