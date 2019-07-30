package com.dm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.user.service.CertConfirmService;

@RestController
@RequestMapping("/confirm")
public class CertConfirmController extends BaseController{

	@Autowired
	private CertConfirmService certConfirmService;
	
	@Autowired
	private LoginUserService userService;
	
	/**
	 * 添加确认人
	 * @return
	 */
	@RequestMapping(value="/selcetByPhone",method=RequestMethod.GET)
	public Result selcetByPhone (String phone) throws Exception{
		LoginUserDetails user = userService.getUserByUsername(phone);
		return ResultUtil.success(null==user?"未注册":user.getUsername());
	}
	
}
