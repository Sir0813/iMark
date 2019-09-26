package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.Opinion;
import com.dm.user.service.OpinionService;
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
@Api(description="意见反馈")
@RestController
@RequestMapping("/opinion")
public class OpinionController extends BaseController {

    @Autowired
    private OpinionService opinionService;

    @ApiOperation(value="意见反馈提交", response= ResultUtil.class)
    @RequestMapping(value = "/submit" ,method = RequestMethod.POST)
    public Result opinionSubmit(@RequestBody Opinion opinion) throws Exception {
        opinionService.opinionSubmit(opinion);
        return ResultUtil.success();
    }

}
