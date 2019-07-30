package com.dm.user.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.User;
import com.dm.user.service.InformationService;
import com.dm.user.service.UserService;
import com.dm.user.util.FileUtil;

@RestController
@RequestMapping
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private InformationService informationService;
	
	@Autowired
	private FileUtil fileUtil;
	
	/**
	 * 发送验证码
	 * @param request
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/sendVeriCode", method = RequestMethod.GET)
	public Result sendVeriCode(String phone) throws Exception {
		String sendVeriCode = userService.sendVeriCode(phone);
		return ResultUtil.success(sendVeriCode);
	}
	
	/**
	 * 注册
	 * @param user 用户对象
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/register", method = RequestMethod.POST)
	public Result register(@RequestBody User user) throws Exception {
		return userService.userRegister(user);
	}
	
	/**
	 * 我的-个人信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/userInfo", method=RequestMethod.GET)
	public Result userInfo() throws Exception {
		return userService.userInfo();
	}
	
	/**
	 * 上传头像
	 * @param request
	 * @param response
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping("/user/uploadHeadPhoto")
	public Result uploadHeadPhoto(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file") MultipartFile multipartFile) {
		return fileUtil.uploadHeadPhoto(request,response,multipartFile);
	}
	
	/**
	 * 重置密码
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/resetPassword", method = RequestMethod.POST)
	public Result resetPassword(@RequestBody Map<String,Object>map) throws Exception {
		return userService.resetPassword(map);
	}
	
	/**
	 * 发送邮箱验证码
	 * @param email 邮箱号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/sendEmailCode", method = RequestMethod.POST)
	public Result sendEmailCode(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.sendEmailCode(map);
	}
	
	/**
	 * 绑定邮箱
	 * @param email 邮箱号
	 * @param veriCode 验证码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/bindEmail", method = RequestMethod.POST)
	public Result bindEmail(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.bindEmail(map);
	}
	
	/**
	 * 更换手机号 校验原手机号验证码
	 * @param oldPhone 原手机号
	 * @param veriCode 验证码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/checkCode", method = RequestMethod.POST)
	public Result checkCode(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.checkCode(map);
	}
	
	/**
	 * 更换手机号
	 * @param newPhone 新手机号
	 * @param veriCode 验证码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/changePhone", method = RequestMethod.POST)
	public Result changePhone(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.changePhone(map);
	}
	
	
}
