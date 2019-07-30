package com.dm.user.service;

import java.util.Map;
import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.User;

public interface UserService {

	/**
	 * 用户注册
	 * @param request
	 * @param user 用户对象
	 * @return
	 * @throws Exception
	 */
	Result userRegister(User user) throws Exception;

	/**
	 * 重置密码
	 * @param passwordReset 密码对象
	 * @return
	 */
	Result resetPassword(Map<String,Object>map) throws Exception;

	/**
	 * 发送短信验证码
	 * @throws Exception
	 */
	String sendVeriCode(String phone) throws Exception;

	/**
	 * 我的-个人信息
	 * @return
	 * @throws Exception
	 */
	Result userInfo() throws Exception;

}
