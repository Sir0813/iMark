package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.BizMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "会话聊天")
@RestController
@RequestMapping(value = "/msg")
public class BizMsgController extends BaseController {

    @Autowired
    private BizMsgService bizMsgService;

    @ApiOperation(value = "聊天列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result msgList() throws Exception {
        return bizMsgService.msgList();
    }

    @ApiOperation(value = "会话内容", response = ResultUtil.class)
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public Result msgDetails(String chatId, String createTime) throws Exception {
        return bizMsgService.msgDetails(chatId, createTime);
    }

}
