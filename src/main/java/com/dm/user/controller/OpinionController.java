package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.Opinion;
import com.dm.user.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opinion")
public class OpinionController extends BaseController {

    @Autowired
    private OpinionService opinionService;

    @RequestMapping(value = "/submit" ,method = RequestMethod.POST)
    public Result opinionSubmit(@RequestBody Opinion opinion) throws Exception {
        opinionService.opinionSubmit(opinion);
        return ResultUtil.success();
    }

}
