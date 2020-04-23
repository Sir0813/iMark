package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.Org;
import com.dm.user.service.OrgService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cui
 * @date 2019-12-25
 */
@Api(description = "组织公证处")
@RestController
@RequestMapping(value = "/org")
public class OrgController extends BaseController {

    @Autowired
    private OrgService orgService;

    @ApiOperation(value = "公证处列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result orgList(Integer pageNum) throws Exception {
        PageInfo<Org> orgList = orgService.orgList(pageNum);
        return ResultUtil.success(orgList);
    }

    @ApiOperation(value = "公证处须知", response = ResultUtil.class)
    @RequestMapping(value = "/notice/{orgId}", method = RequestMethod.GET)
    public Result notice(@PathVariable int orgId) throws Exception {
        String serveDesc = orgService.notice(orgId);
        return ResultUtil.success(serveDesc);
    }

}
