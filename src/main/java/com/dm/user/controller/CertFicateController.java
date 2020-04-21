package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.TemCertFile;
import com.dm.user.service.CertFicateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Api(description = "存证")
@RestController
@RequestMapping(value = "/certficate")
public class CertFicateController extends BaseController {

    @Autowired
    private CertFicateService certFicateService;

    @ApiOperation(value = "存证", response = ResultUtil.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result saveCert(@Valid @RequestBody CertFicate certFicate) throws Exception {
        CertFicate cert = certFicateService.saveCert(certFicate);
        return null == cert ? ResultUtil.info("file.upload.no.file.code", "file.upload.no.file.msg") : ResultUtil.success(cert);
    }

    @ApiOperation(value = "模板存证存草稿", response = ResultUtil.class)
    @RequestMapping(value = "/temSave", method = RequestMethod.POST)
    public Result saveTemplate(@RequestBody TemCertFile temCertFile) throws Exception {
        CertFicate cert = certFicateService.saveTemplate(temCertFile);
        return ResultUtil.success(cert);
    }

    /*@ApiOperation(value = "我的存证", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result listCerts(Page<CertFicate> page, String state, String certName) throws Exception {
        PageInfo<CertFicate> certFicateList = certFicateService.listCerts(page, state, certName);
        return ResultUtil.success(certFicateList);
    }*/

    @ApiOperation(value = "我的存证", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result myList(Page<CertFicate> page, String state, String certName) throws Exception {
        PageInfo<CertFicate> certFicateList = certFicateService.myCertList(page, state, certName);
        return ResultUtil.success(certFicateList);
    }

    @ApiOperation(value = "存证详情", response = ResultUtil.class)
    @RequestMapping(value = "/details/{certFicateId}", method = RequestMethod.GET)
    public Result certDetails(@PathVariable String certFicateId) throws Exception {
        CertFicate cert = certFicateService.certDetails(certFicateId);
        return ResultUtil.success(cert);
    }

    @ApiOperation(value = "撤回待他人确认存证", response = ResultUtil.class)
    @RequestMapping(value = "/revoke/{certId}", method = RequestMethod.GET)
    public Result certRevoke(@PathVariable String certId) throws Exception {
        certFicateService.certRevoke(certId);
        return ResultUtil.success();
    }

    @ApiOperation(value = "退回待自己确认存证", response = ResultUtil.class)
    @RequestMapping(value = "/returnReason", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reason", value = "退回存证原因", dataType = "String")
    })
    public Result returnReason(@RequestBody Map<String, Object> map) throws Exception {
        certFicateService.returnReason(map);
        return ResultUtil.success();
    }

    @ApiOperation(value = "确认待自己确认", response = ResultUtil.class)
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certId", value = "存证ID", dataType = "Integer"),
            @ApiImplicitParam(name = "signature", value = "签名值", dataType = "String"),
            @ApiImplicitParam(name = "aid", value = "签名值", dataType = "String")
    })
    public Result confirm(@RequestBody Map<String, Object> map) throws Exception {
        certFicateService.confirm(map);
        return ResultUtil.success();
    }

    @ApiOperation(value = "草稿删除", response = ResultUtil.class)
    @RequestMapping(value = "/draftDel", method = RequestMethod.POST)
    public Result draftDel(@RequestBody CertFicate certFicate) throws Exception {
        certFicateService.draftDel(certFicate);
        return ResultUtil.success();
    }

    @ApiOperation(value = "获取存证证书", response = ByteArrayResource.class)
    @RequestMapping(value = "/getCertImg/{certId}", method = RequestMethod.GET, headers = "Accept=image/jpeg")
    public ByteArrayResource getCertImg(@PathVariable String certId) throws Exception {
        ByteArrayResource bar = certFicateService.getCertImg(certId);
        return bar;
    }

}
