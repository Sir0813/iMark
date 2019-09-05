package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.User;
import com.dm.user.entity.UserCard;
import com.dm.user.service.InformationService;
import com.dm.user.service.UserCardService;
import com.dm.user.service.UserService;
import com.dm.user.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private InformationService informationService;
	
	@Autowired
	private FileUtil fileUtil;

	@Autowired
	private UserCardService userCardService;
	
	/**
	 * 发送验证码
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
	 * 找回密码 验证验证码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/nextOperate", method = RequestMethod.POST)
	public Result nextOperate(@RequestBody Map<String,Object>map) throws Exception {
		return userService.nextOperate(map);
	}

	/**
	 * 找回密码
	 * @param phone 手机号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/retrievePwd", method = RequestMethod.POST)
	public Result retrievePwd(@RequestBody Map<String,Object>map) throws Exception {
		return userService.retrievePwd(map);
	}

	/**
	 * 极光推送获取用户 推送ID
	 * @param registrationId 一个ID对应一个用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/getRegistrationId", method = RequestMethod.POST)
	public Result getRegistrationId(@RequestBody Map<String,Object>map) throws Exception{
		userService.getRegistrationId(map);
		return ResultUtil.success();
	}

    /**
     * 登录后获取离线推送消息
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/user/getPushMsg", method = RequestMethod.GET)
    public Result getPushMsg() throws Exception{
        return userService.getPushMsg();
    }

	/**
	 * 我的-认证信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/userInfo", method=RequestMethod.GET)
	public Result userInfo() throws Exception {
		return userService.userInfo();
	}

	/**
	 * 我的-个人信息(修改)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/update", method=RequestMethod.POST)
	public Result userUpdate(@RequestBody  User user) throws Exception {
		return userService.userUpdate(user);
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

	/**
	 * 实名认证
	 * @param userCard
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/authentication", method = RequestMethod.POST)
	public Result authentication(@RequestBody UserCard userCard) throws Exception {
		return userCardService.authentication(userCard);
	}

	/**
	 * 实名信息查看
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/user/real/info", method = RequestMethod.GET)
	public Result realInfo() throws Exception {
		return userCardService.realInfo();
	}

	/**
	 * 短信验证码登录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/api/dynamic/login", method = RequestMethod.POST)
	public Result dynamicLogin(@RequestBody Map<String,Object>map) throws Exception {
		return userService.dynamicLogin(map);
	}

}
