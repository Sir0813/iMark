package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.App;
import com.dm.user.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController extends BaseController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/appVersion" , method = RequestMethod.POST)
    public Result appVersion(@RequestBody App app) throws Exception{
        return appService.appVersion(app);
    }

}
