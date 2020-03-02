package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.OrgItems;
import com.dm.user.service.OrgItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cui
 * @date 2019-12-25
 */
@Api(description = "公证处业务项目")
@RestController
@RequestMapping(value = "/item")
public class OrgItemController extends BaseController {

    @Autowired
    private OrgItemService orgItemService;

    @ApiOperation(value = "公证业务列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result itemList(Page<OrgItems> page, int orgId) throws Exception {
        PageInfo<OrgItems> orgItemsList = orgItemService.itemList(page, orgId);
        return ResultUtil.success(orgItemsList);
    }

    @ApiOperation(value = "申请公正内容填充展示", response = ResultUtil.class)
    @RequestMapping(value = "/content/{itemId}", method = RequestMethod.GET)
    public Result content(@PathVariable int itemId) throws Exception {
        Map<String, Object> map = orgItemService.content(itemId);
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "公证类型", response = ResultUtil.class)
    @RequestMapping(value = "/type/list", method = RequestMethod.GET)
    public Result typeList() throws Exception {
        return orgItemService.typeList();
    }

}
