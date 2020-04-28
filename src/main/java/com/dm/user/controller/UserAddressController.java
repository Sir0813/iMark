package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.UserAddress;
import com.dm.user.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "用户地址管理")
@RestController
@RequestMapping(value = "/user/address")
public class UserAddressController extends BaseController {

    @Autowired
    private UserAddressService userAddressService;

    @ApiOperation(value = "列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list() throws Exception {
        List<UserAddress> userAddressList = userAddressService.list(LoginUserHelper.getUserId());
        return ResultUtil.success(userAddressList);
    }

    @ApiOperation(value = "新增", response = ResultUtil.class)
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody UserAddress userAddress) throws Exception {
        userAddressService.insert(userAddress);
        return ResultUtil.success();
    }

    @ApiOperation(value = "删除", response = ResultUtil.class)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Result delete(@PathVariable Integer id) throws Exception {
        userAddressService.delete(id);
        return ResultUtil.success();
    }

}
