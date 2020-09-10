package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ApplyFeeView;
import com.dm.user.entity.ItemApply;
import com.dm.user.entity.ProcessConfigView;
import com.dm.user.entity.Reject;
import com.dm.user.service.ItemApplyService;
import com.dm.user.service.PubDictoryService;
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

    @Autowired
    private PubDictoryService pubDictoryService;

    @ApiOperation(value = "公正首页接口", response = ResultUtil.class)
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Result index() throws Exception {
        Result index = itemApplyService.index();
        return index;
    }

    @ApiOperation(value = "申请公正", response = ResultUtil.class)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.insert(itemApply);
    }

    @ApiOperation(value = "公正申请改为草稿", response = ResultUtil.class)
    @RequestMapping(value = "/updateToDraft", method = RequestMethod.POST)
    public Result updateToDraft(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.updateToDraft(itemApply);
    }

    @ApiOperation(value = "支付宝--返回支付订单信息", response = ResultUtil.class)
    @RequestMapping(value = "/first/alipay", method = RequestMethod.POST)
    public Result firstAlipay(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.firstAlipay(map);
    }

    @ApiOperation(value = "确认提交订单(首款)", response = ResultUtil.class)
    @RequestMapping(value = "/submitApply", method = RequestMethod.POST)
    public Result submitApply(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.submitApply(itemApply);
    }

    @ApiOperation(value = "支付尾款", response = ResultUtil.class)
    @RequestMapping(value = "/payBalance", method = RequestMethod.POST)
    public Result payBalance(@RequestBody ApplyFeeView applyFeeView) throws Exception {
        return itemApplyService.payBalance(applyFeeView);
    }

    @ApiOperation(value = "我的公正列表", response = ResultUtil.class)
    @RequestMapping(value = {"/list/{pageNum}/{type}", "/list/{pageNum}/{type}/{word}"}, method = RequestMethod.GET)
    public Result list(@PathVariable int pageNum, @PathVariable int type, @PathVariable(required = false) String word) throws Exception {
        PageInfo<ItemApply> itemApplyList = itemApplyService.list(pageNum, type, word);
        return ResultUtil.success(itemApplyList);
    }

    @ApiOperation(value = "详情", response = ResultUtil.class)
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

    @ApiOperation(value = "补充-修改材料", response = ResultUtil.class)
    @RequestMapping(value = "/add/files/{applyid}", method = RequestMethod.GET)
    public Result addFiles(@PathVariable int applyid) throws Exception {
        return itemApplyService.addFiles(applyid);
    }

    @ApiOperation(value = "受理进度", response = ResultUtil.class)
    @RequestMapping(value = "/accept/progress/{applyid}", method = RequestMethod.GET)
    public Result acceptProgress(@PathVariable int applyid) throws Exception {
        return itemApplyService.acceptProgress(applyid);
    }

    /**
     * 公正人员接口
     */

    @ApiOperation(value = "我的任务首页", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/index", method = RequestMethod.GET)
    public Result myTaskIndex() throws Exception {
        return itemApplyService.myTaskIndex();
    }

    @ApiOperation(value = "我的任务列表", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/list", method = RequestMethod.GET)
    public Result myTaskList(@RequestParam int pageNum, @RequestParam int type, @RequestParam(required = false) Integer itemId, @RequestParam(required = false) String word) throws Exception {
        return itemApplyService.myTaskList(pageNum, type, itemId, word);
    }

    @ApiOperation(value = "公正复核待审批-已审批列表", response = ResultUtil.class)
    @RequestMapping(value = "/review/list", method = RequestMethod.GET)
    public Result reviewList(@RequestParam Integer pageNum, @RequestParam String word, @RequestParam int type, @RequestParam int itemId) throws Exception {
        return itemApplyService.reviewList(pageNum, word, type, itemId);
    }

    @ApiOperation(value = "公正管理端详情", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/detail/{applyid}", method = RequestMethod.GET)
    public Result mytaskDetail(@PathVariable int applyid) throws Exception {
        return itemApplyService.mytaskDetail(applyid);
    }

    @ApiOperation(value = "预审通过", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/passed/{applyid}", method = RequestMethod.GET)
    public Result mytaskPassed(@PathVariable int applyid) throws Exception {
        return itemApplyService.mytaskPassed(applyid);
    }

    @ApiOperation(value = "预审驳回退费详情信息", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/reject/detail/{applyid}", method = RequestMethod.GET)
    public Result rejectDetail(@PathVariable int applyid) throws Exception {
        return itemApplyService.rejectDetail(applyid);
    }

    @ApiOperation(value = "预审-已接单驳回", response = ResultUtil.class)
    @RequestMapping(value = "/mytask/reject/reason", method = RequestMethod.POST)
    public Result mytaskRejectReason(@RequestBody Reject reject) throws Exception {
        return itemApplyService.mytaskRejectReason(reject);
    }

    @ApiOperation(value = "接单", response = ResultUtil.class)
    @RequestMapping(value = "/take/order", method = RequestMethod.POST)
    public Result takeOrder(@RequestBody ItemApply itemApply) throws Exception {
        return itemApplyService.takeOrder(itemApply);
    }

    @ApiOperation(value = "结算尾款-维护的收费列表", response = ResultUtil.class)
    @RequestMapping(value = "/otherfee/list", method = RequestMethod.GET)
    public Result otherFeeList() throws Exception {
        return itemApplyService.otherFeeList();
    }

    @ApiOperation(value = "结算尾款-修改费用保存", response = ResultUtil.class)
    @RequestMapping(value = "/otherfee/save", method = RequestMethod.POST)
    public Result otherFeeSave(@RequestBody ApplyFeeView applyFeeView) throws Exception {
        return itemApplyService.otherFeeSave(applyFeeView);
    }

    @ApiOperation(value = "结算尾款-通知用户缴费", response = ResultUtil.class)
    @RequestMapping(value = "/notice/pay", method = RequestMethod.POST)
    public Result noticePay(@RequestBody ItemApply itemapply) throws Exception {
        return itemApplyService.noticePay(itemapply);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "describe", value = "批注内容", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "文件ID", dataType = "int"),
            @ApiImplicitParam(name = "aid", value = "用户aid", dataType = "String"),
            @ApiImplicitParam(name = "signature", value = "签名值", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "文件ID", dataType = "int"),
            @ApiImplicitParam(name = "fileStatus", value = "信息是否有误 0 有误 1 无误", dataType = "int"),
            @ApiImplicitParam(name = "fileIndex", value = "信息有误时插入", dataType = "String")
    })
    @ApiOperation(value = "文件添加修改批注", response = ResultUtil.class)
    @RequestMapping(value = "/notes", method = RequestMethod.POST)
    public Result notes(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.notes(map);
    }

    @ApiOperation(value = "文件批注记录", response = ResultUtil.class)
    @RequestMapping(value = "/notes/list/{id}", method = RequestMethod.GET)
    public Result notesList(@PathVariable int id) throws Exception {
        return itemApplyService.notesList(id);
    }

    @ApiOperation(value = "审批流程模板", response = ResultUtil.class)
    @RequestMapping(value = "/process/template", method = RequestMethod.GET)
    public Result processTemplate() throws Exception {
        return itemApplyService.processTemplate();
    }

    @ApiOperation(value = "新增审批人-选择列表", response = ResultUtil.class)
    @RequestMapping(value = "/process/list", method = RequestMethod.GET)
    public Result processList() throws Exception {
        return itemApplyService.processList();
    }

    @ApiOperation(value = "审批流程保存-申请审批", response = ResultUtil.class)
    @RequestMapping(value = "/process/save", method = RequestMethod.POST)
    public Result processSave(@RequestBody ProcessConfigView processConfigView) throws Exception {
        return itemApplyService.processSave(processConfigView);
    }

    @ApiOperation(value = "审批进度", response = ResultUtil.class)
    @RequestMapping(value = "/approval/progress/{applyid}", method = RequestMethod.GET)
    public Result approvalProgress(@PathVariable Integer applyid) throws Exception {
        return itemApplyService.approvalProgress(applyid);
    }

    @ApiOperation(value = "审批进度中查看意见书", response = ResultUtil.class)
    @RequestMapping(value = "/see/book/{id}/{type}", method = RequestMethod.GET)
    public Result seeBook(@PathVariable Integer id, @PathVariable int type) throws Exception {
        return itemApplyService.seeBook(id, type);
    }

    @ApiOperation(value = "审批通过", response = ResultUtil.class)
    @RequestMapping(value = "/approved", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyid", value = "公正ID", dataType = "int"),
            @ApiImplicitParam(name = "opinion", value = "审批通过意见", dataType = "String"),
            @ApiImplicitParam(name = "fileString", value = "意见书内容", dataType = "String")
    })
    public Result approved(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.approved(map);
    }

    @ApiOperation(value = "审批退回", response = ResultUtil.class)
    @RequestMapping(value = "/reject/reason", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyid", value = "公正ID", dataType = "int"),
            @ApiImplicitParam(name = "opinion", value = "审批退回意见", dataType = "String"),
            @ApiImplicitParam(name = "fileString", value = "意见书内容", dataType = "String")
    })
    public Result rejectReason(@RequestBody Map<String, Object> map) throws Exception {
        return itemApplyService.rejectReason(map);
    }

    @ApiOperation(value = "制证完成", response = ResultUtil.class)
    @RequestMapping(value = "/success/{applyid}", method = RequestMethod.GET)
    public Result isSuccess(@PathVariable int applyid) throws Exception {
        return itemApplyService.isSuccess(applyid);
    }

    @ApiOperation(value = "国家", response = ResultUtil.class)
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Result country() throws Exception {
        return pubDictoryService.selectCountry();
    }

    @ApiOperation(value = "国家下语言", response = ResultUtil.class)
    @RequestMapping(value = "/language/{id}", method = RequestMethod.GET)
    public Result language(@PathVariable Integer id) throws Exception {
        return pubDictoryService.selectCountryLanguage(id);
    }

    /*@ApiOperation(value = "用户材料审核通过", response = ResultUtil.class)
    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    public Result pass(@RequestBody ItemApplyFiles itemApplyFiles) throws Exception {
        return itemApplyService.pass(itemApplyFiles);
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
    }*/


}
