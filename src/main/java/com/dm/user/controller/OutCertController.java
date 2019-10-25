package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.OutCert;
import com.dm.user.service.OutCertService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cui
 * @date 2019-09-26
 */
@Api(description = "出证")
@RestController
@RequestMapping(value = "/out")
public class OutCertController extends BaseController {

    @Autowired
    private OutCertService outCertService;

    @ApiOperation(value = "动态下载出证模板", response = ResultUtil.class)
    @RequestMapping(value = "/cert/template", method = RequestMethod.GET)
    public Result downloadOutCertTemplate(String certIds) throws Exception {
        String downloadPath = outCertService.downloadOutCertTemplate(certIds);
        return ResultUtil.success(downloadPath);
    }

    @ApiOperation(value = "出证提交", response = ResultUtil.class)
    @RequestMapping(value = "/cert/save", method = RequestMethod.POST)
    public Result save(@RequestBody OutCert outCert) throws Exception {
        outCertService.save(outCert);
        return ResultUtil.success();
    }

    @ApiOperation(value = "出证列表", response = ResultUtil.class)
    @RequestMapping(value = "/cert/list", method = RequestMethod.GET)
    public Result list(Page<OutCert> page, String state) throws Exception {
        PageInfo<OutCert> list = outCertService.list(page, state);
        return ResultUtil.success(list);
    }

    @ApiOperation(value = "出证详情", response = ResultUtil.class)
    @RequestMapping(value = "/cert/details", method = RequestMethod.GET)
    public Result details(String outCertId) throws Exception {
        OutCert outCert = outCertService.details(outCertId);
        return ResultUtil.success(outCert);
    }

    @ApiOperation(value = "出证文件打包下载", response= ResultUtil.class)
    @RequestMapping(value = "/cert/downZip", method = RequestMethod.GET)
    public Result downZip(String outCertId) throws Exception {
        String downPath = outCertService.downZip(outCertId);
        return ResultUtil.success(downPath);
    }

}
