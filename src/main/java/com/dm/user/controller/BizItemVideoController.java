package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizItemVideo;
import com.dm.user.service.BizItemVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cui
 * @date 2020-04-08
 */
@Api(description = "视频通话")
@RestController
@RequestMapping(value = "/item/video")
public class BizItemVideoController extends BaseController {

    @Autowired
    private BizItemVideoService bizItemVideoService;

    @ApiOperation(value = "预约公正视频时间", response = ResultUtil.class)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result appVersion(@RequestBody BizItemVideo bizItemVideo) throws Exception {
        return bizItemVideoService.insertData(bizItemVideo);
    }

}
