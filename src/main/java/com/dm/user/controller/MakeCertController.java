package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizCertModel;
import com.dm.user.entity.ItemApply;
import com.dm.user.service.BizCertService;
import com.dm.user.service.ItemApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(description = "制证")
@RestController
@RequestMapping(value = "/make")
public class MakeCertController extends BaseController {

    @Autowired
    private ItemApplyService itemApplyService;

    @Autowired
    private BizCertService bizCertService;

    @ApiOperation(value = "制证模板", response = ResultUtil.class)
    @RequestMapping(value = "/cert/template/{applyId}", method = RequestMethod.GET)
    public Result certTemplate(@PathVariable Integer applyId) throws Exception {
        ItemApply itemApply = itemApplyService.selectById(applyId);
        BizCertModel bizCertModel = bizCertService.selectByItemId(itemApply.getItemid());
        return ResultUtil.success(bizCertModel);
    }

    @ApiOperation(value = "制证模板", response = ResultUtil.class)
    @RequestMapping(value = "/cert/info/{applyId}", method = RequestMethod.GET)
    public Result certInfo(@PathVariable Integer applyId) throws Exception {
        Map<String, Object> map = bizCertService.certInfo(applyId);
        return ResultUtil.success();
    }

}
