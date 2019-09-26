package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.App;
import com.dm.user.service.AppService;
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
@Api(description="APP版本")
@RestController
@RequestMapping("/api")
public class AppController extends BaseController {

    @Autowired
    private AppService appService;

    @ApiOperation(value="APP获取最新版本", response= ResultUtil.class)
    @RequestMapping(value = "/appVersion" , method = RequestMethod.POST)
    public Result appVersion(@RequestBody App app) throws Exception{
        return appService.getAppVersion(app);
    }

}
