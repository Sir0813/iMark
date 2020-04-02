package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ItemApply;
import com.dm.user.entity.ItemApplyFiles;
import com.dm.user.service.ItemApplyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author cui
 * @date 2020-01-02
 */
@Api(description = "公正")
@RestController
@RequestMapping(value = "/itemapply")
public class ItemApplyController extends BaseController {

    @Autowired
    private ItemApplyService itemApplyService;

    @ApiOperation(value = "用户办理公正申请", response = ResultUtil.class)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.insert(itemApply);
    }

    @ApiOperation(value = "公正改为草稿", response = ResultUtil.class)
    @RequestMapping(value = "/updateToDraft", method = RequestMethod.POST)
    public Result updateToDraft(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.updateToDraft(itemApply);
    }

    @ApiOperation(value = "支付金额 提交申请", response = ResultUtil.class)
    @RequestMapping(value = "/submitApply", method = RequestMethod.POST)
    public Result submitApply(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.submitApply(itemApply);
    }

    @ApiOperation(value = "支付尾款提交", response = ResultUtil.class)
    @RequestMapping(value = "/payBalance", method = RequestMethod.POST)
    public Result payBalance(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.payBalance(itemApply);
    }

    @ApiOperation(value = "用户已经提交的公正申请列表", response = ResultUtil.class)
    @RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
    public Result list(@PathVariable int pageNum) throws Exception {
        PageInfo<ItemApply> itemApplyList = itemApplyService.list(pageNum);
        return ResultUtil.success(itemApplyList);
    }

    @ApiOperation(value = "用户已经提交的公正申请详情及办理进度", response = ResultUtil.class)
    @RequestMapping(value = "/detail/{applyid}", method = RequestMethod.GET)
    public Result detail(@PathVariable int applyid) throws Exception {
        Map<String, Object> map = itemApplyService.detail(applyid);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "删除草稿", response = ResultUtil.class)
    @RequestMapping(value = "/delete/{applyid}", method = RequestMethod.GET)
    public Result delete(@PathVariable int applyid) throws Exception {
        return itemApplyService.delete(applyid);
    }

    /**
     * 公正人员接口
     */
    @ApiOperation(value = "待接单列表", response = ResultUtil.class)
    @RequestMapping(value = "/pending/list", method = RequestMethod.GET)
    public Result pendList(Page<ItemApply> page, int itemId) throws Exception {
        return itemApplyService.pendList(page, itemId);
    }

    @ApiOperation(value = "接单", response = ResultUtil.class)
    @RequestMapping(value = "/take/order", method = RequestMethod.POST)
    public Result takeOrder(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.takeOrder(itemApply);
    }

    @ApiOperation(value = "公正发送通知", response = ResultUtil.class)
    @RequestMapping(value = "/submit/customPrice", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyid", value = "ID", dataType = "int"),
            @ApiImplicitParam(name = "price", value = "退回原因", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "退回类型 1 自定义价格通知 2 标的价格通知", dataType = "int")
    })
    public Result submitCustomPrice(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.submitCustomPrice(map);
    }

    @ApiOperation(value = "资料待审核列表", response = ResultUtil.class)
    @RequestMapping(value = "/pending/review", method = RequestMethod.GET)
    public Result pendReview(Page<ItemApply> page, int itemId, int type) throws Exception {
        return itemApplyService.pendReview(page, itemId, type);
    }

    @ApiOperation(value = "用户材料退回", response = ResultUtil.class)
    @RequestMapping(value = "/reject/reason", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyid", value = "ID", dataType = "int"),
            @ApiImplicitParam(name = "rejectReason", value = "退回原因", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "退回类型 1 材料退回 2 审批流程退回", dataType = "int")
    })
    public Result rejectReason(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.rejectReason(map);
    }

    @ApiOperation(value = "用户材料审核通过", response = ResultUtil.class)
    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    public Result pass(@RequestBody ItemApplyFiles itemApplyFiles) throws Exception {
        return itemApplyService.pass(itemApplyFiles);
    }

    @ApiOperation(value = "文件添加修改批注", response = ResultUtil.class)
    @RequestMapping(value = "/notes", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "describe", value = "批注内容", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "ID", dataType = "int"),
            @ApiImplicitParam(name = "aid", value = "用户aid", dataType = "String"),
            @ApiImplicitParam(name = "signature", value = "签名值", dataType = "String")
    })
    public Result notes(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.notes(map);
    }

    @ApiOperation(value = "审批待处理", response = ResultUtil.class)
    @RequestMapping(value = "/wait/list", method = RequestMethod.GET)
    public Result list(Page<ItemApply> page, int itemId) throws Exception {
        PageInfo<ItemApply> itemApplyList = itemApplyService.waitList(page, itemId);
        return ResultUtil.success(itemApplyList);
    }

    @ApiOperation(value = "审批已处理", response = ResultUtil.class)
    @RequestMapping(value = "/deal/list", method = RequestMethod.GET)
    public Result dealList(Page<ItemApply> page, int itemId) throws Exception {
        PageInfo<ItemApply> itemApplyList = itemApplyService.dealList(page, itemId);
        return ResultUtil.success(itemApplyList);
    }

    @ApiOperation(value = "审批通过", response = ResultUtil.class)
    @RequestMapping(value = "/approved", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyid", value = "ID", dataType = "int"),
            @ApiImplicitParam(name = "opinion", value = "审批意见", dataType = "String")
    })
    public Result approved(@RequestBody Map<String, Object> map) throws Exception {
        itemApplyService.approved(map);
        return ResultUtil.success();
    }


}
