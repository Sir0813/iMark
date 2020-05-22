package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ItemNotice;
import com.dm.user.entity.ItemNoticeApply;
import com.dm.user.service.ItemNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(description = "材料签署告知书")
@RestController
@RequestMapping(value = "/notice")
public class ItemNoticeController extends BaseController {

    @Autowired
    private ItemNoticeService itemNoticeService;

    @ApiOperation(value = "材料签署列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result noticeList() throws Exception {
        List<ItemNotice> itemNoticeList = itemNoticeService.noticeList();
        return ResultUtil.success(itemNoticeList);
    }

    @ApiOperation(value = "材料进行签署", response = ResultUtil.class)
    @RequestMapping(value = "/signature", method = RequestMethod.POST)
    public Result signature(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        List<ItemNoticeApply> itemNoticeApplyList = itemNoticeService.signature(request, response, multipartFile);
        return ResultUtil.success(itemNoticeApplyList);
    }

}
