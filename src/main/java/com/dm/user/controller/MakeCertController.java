package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizCertModel;
import com.dm.user.entity.ItemApply;
import com.dm.user.entity.ItemApplyFiles;
import com.dm.user.entity.TemplateSymbol;
import com.dm.user.service.BizCertService;
import com.dm.user.service.ItemApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @ApiOperation(value = "制证字段信息", response = ResultUtil.class)
    @RequestMapping(value = "/cert/info/{applyId}", method = RequestMethod.GET)
    public Result certInfo(@PathVariable Integer applyId) throws Exception {
        Map<String, Object> map = bizCertService.certInfo(applyId);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "制证意见书信息提交", response = ResultUtil.class)
    @RequestMapping(value = "/cert/template/submit", method = RequestMethod.POST)
    public Result certSubmit(@RequestBody ItemApplyFiles itemApplyFiles) throws Exception {
        return bizCertService.submit(itemApplyFiles);
    }

    @ApiOperation(value = "审批意见书信息提交", response = ResultUtil.class)
    @RequestMapping(value = "/cert/review/submit", method = RequestMethod.POST)
    public Result reviewSubmit(@RequestBody ItemApplyFiles itemApplyFiles) throws Exception {
        return bizCertService.reviewSubmit(itemApplyFiles);
    }

    @ApiOperation(value = "意见书占位符", response = ResultUtil.class)
    @RequestMapping(value = "/cert/template/symbol", method = RequestMethod.GET)
    public Result templateSymbol() throws Exception {
        List<TemplateSymbol> templateSymbolList = new ArrayList<>();
        TemplateSymbol templateSymbol = new TemplateSymbol();
        templateSymbol.setName("itemName");
        templateSymbol.setValue("项目名称");
        TemplateSymbol templateSymbol2 = new TemplateSymbol();
        templateSymbol2.setName("orgName");
        templateSymbol2.setValue("公证处名称");
        TemplateSymbol templateSymbol3 = new TemplateSymbol();
        templateSymbol3.setName("idCard");
        templateSymbol3.setValue("身份证号码");
        TemplateSymbol templateSymbol4 = new TemplateSymbol();
        templateSymbol4.setName("realName");
        templateSymbol4.setValue("申请人姓名");
        templateSymbolList.add(templateSymbol);
        templateSymbolList.add(templateSymbol2);
        templateSymbolList.add(templateSymbol3);
        templateSymbolList.add(templateSymbol4);
        return ResultUtil.success(templateSymbolList);
    }

}
