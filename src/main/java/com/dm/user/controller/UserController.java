package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.PushMsg;
import com.dm.user.entity.User;
import com.dm.user.entity.UserCard;
import com.dm.user.service.InformationService;
import com.dm.user.service.PushMsgService;
import com.dm.user.service.UserCardService;
import com.dm.user.service.UserService;
import com.dm.user.util.FileUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(description="用户")
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

	@Autowired
	private PushMsgService pushMsgService;
	
	@ApiOperation(value="发送验证码", response= ResultUtil.class)
	@RequestMapping(value="/api/sendVeriCode", method = RequestMethod.GET)
	public Result sendVeriCode( String phone) throws Exception {
		String sendVeriCode = userService.sendVeriCode(phone);
		return ResultUtil.success(sendVeriCode);
	}
	
	@ApiOperation(value="用户注册", response= ResultUtil.class)
	@RequestMapping(value="/api/register", method = RequestMethod.POST)
	public Result register(@RequestBody User user) throws Exception {
		return userService.userRegister(user);
	}

	@ApiOperation(value="找回密码 下一步 (验证验证码)", response= ResultUtil.class)
	@RequestMapping(value="/api/nextOperate", method = RequestMethod.POST)
	public Result nextOperate(@RequestBody Map<String,Object>map) throws Exception {
		return userService.nextOperate(map);
	}

	@ApiOperation(value="找回密码", response= ResultUtil.class)
	@RequestMapping(value="/api/retrievePwd", method = RequestMethod.POST)
	public Result retrievePwd(@RequestBody Map<String,Object>map) throws Exception {
		return userService.retrievePwd(map);
	}

	@ApiOperation(value="极光推送获取用户 推送ID", response= ResultUtil.class)
	@RequestMapping(value="/user/getRegistrationId", method = RequestMethod.POST)
	public Result getRegistrationId(@RequestBody Map<String,Object>map) throws Exception{
		String userId = userService.getRegistrationId(map);
		return ResultUtil.success(userId);
	}

	@ApiOperation(value="获取离线推送消息", response= ResultUtil.class)
    @RequestMapping(value="/user/getPushMsg", method = RequestMethod.GET)
    public Result getPushMsg() throws Exception{
        return userService.getPushMsg();
    }

	@ApiOperation(value="我的-认证信息", response= ResultUtil.class)
	@RequestMapping(value="/user/userInfo", method=RequestMethod.GET)
	public Result userInfo() throws Exception {
		return userService.userInfo();
	}

	@ApiOperation(value="个人信息(修改)", response= ResultUtil.class)
	@RequestMapping(value="/user/update", method=RequestMethod.POST)
	public Result userUpdate(@RequestBody  User user) throws Exception {
		return userService.userUpdate(user);
	}

	@ApiOperation(value="上传头像", response= ResultUtil.class)
	@RequestMapping(value="/user/uploadHeadPhoto", method = RequestMethod.POST)
	public Result uploadHeadPhoto(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file") MultipartFile multipartFile) {
		return fileUtil.uploadHeadPhoto(request,response,multipartFile);
	}
	
	@ApiOperation(value="重置密码", response= ResultUtil.class)
	@RequestMapping(value="/user/resetPassword", method = RequestMethod.POST)
	public Result resetPassword(@RequestBody Map<String,Object>map) throws Exception {
		return userService.resetPassword(map);
	}
	
	@ApiOperation(value="发送邮箱验证码", response= ResultUtil.class)
	@RequestMapping(value="/user/sendEmailCode", method = RequestMethod.POST)
	public Result sendEmailCode(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.sendEmailCode(map);
	}
	
	@ApiOperation(value="绑定邮箱", response= ResultUtil.class)
	@RequestMapping(value="/user/bindEmail", method = RequestMethod.POST)
	public Result bindEmail(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.bindEmail(map);
	}
	
	@ApiOperation(value="更换手机号 校验原手机号验证码", response= ResultUtil.class)
	@RequestMapping(value="/user/checkCode", method = RequestMethod.POST)
	public Result checkCode(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.checkCode(map);
	}
	
	@ApiOperation(value="更换手机号", response= ResultUtil.class)
	@RequestMapping(value="/user/changePhone", method = RequestMethod.POST)
	public Result changePhone(@RequestBody Map<String,Object>map) throws Exception {
		return informationService.changePhone(map);
	}

	@ApiOperation(value="实名认证提交", response= ResultUtil.class)
	@RequestMapping(value="/user/authentication", method = RequestMethod.POST)
	public Result authentication(@RequestBody UserCard userCard) throws Exception {
		return userCardService.authentication(userCard);
	}

	@ApiOperation(value="实名信息查看", response= ResultUtil.class)
	@RequestMapping(value="/user/real/info", method = RequestMethod.GET)
	public Result realInfo() throws Exception {
		return userCardService.realInfo();
	}

	@ApiOperation(value="短信验证码登录", response= ResultUtil.class)
	@RequestMapping(value="/api/dynamic/login", method = RequestMethod.POST)
	public Result dynamicLogin(@RequestBody Map<String,Object>map) throws Exception {
		return userService.dynamicLogin(map);
	}

	@ApiOperation(value="用户消息", response= ResultUtil.class)
	@RequestMapping(value="/user/history/info", method = RequestMethod.GET)
	public Result historyInfo(Page<PushMsg> page) throws Exception {
		PageInfo<PushMsg> pageInfo = pushMsgService.historyInfo(page);
		return ResultUtil.success(pageInfo);
	}

	@ApiOperation(value="读取未查看消息", response= ResultUtil.class)
	@RequestMapping(value="/user/readInfo", method = RequestMethod.GET)
	public Result readInfo(String pushId) throws Exception {
		pushMsgService.readInfo(pushId);
		return ResultUtil.success();
	}

}
