package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.user.service.OutCertService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*@ApiOperation(value = "动态下载出证模板", response = ResultUtil.class)
    @RequestMapping(value = "/cert/template/{certIds}", method = RequestMethod.GET)
    public Result downloadOutCertTemplate(@PathVariable String certIds) throws Exception {
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
    @RequestMapping(value = "/cert/details/{outCertId}", method = RequestMethod.GET)
    public Result details(@PathVariable String outCertId) throws Exception {
        OutCert outCert = outCertService.details(outCertId);
        return ResultUtil.success(outCert);
    }

    @ApiOperation(value = "出证文件打包下载", response = ResultUtil.class)
    @RequestMapping(value = "/cert/downZip/{outCertId}", method = RequestMethod.GET)
    public Result downZip(@PathVariable String outCertId) throws Exception {
        String downPath = outCertService.downZip(outCertId);
        return ResultUtil.success(downPath);
    }*/

}
