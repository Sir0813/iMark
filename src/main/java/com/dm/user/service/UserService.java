package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.User;

import java.util.Map;

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

	/**
	 * 登录后获取离线推送消息
	 * @return
	 * @throws Exception
	 */
	Result getPushMsg() throws Exception;

	/**
	 * 极光绑定关系
	 * @param map
	 */
	String getRegistrationId(Map<String, Object> map) throws Exception;

	/**
	 * 我的-个人信息(修改)
	 * @return
	 * @throws Exception
	 */
	Result userUpdate(User user)throws Exception;

	/**
	 * 修改密码
	 * @param map
	 * @return
	 * @throws Exception
	 */
    Result retrievePwd(Map<String, Object> map) throws Exception;

	/**
	 * 找回密码下一步
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Result nextOperate(Map<String, Object> map) throws Exception;

	/**
	 * 动态密码登录
	 * @param map
	 * @return
	 * @throws Exception
	 */
    Result dynamicLogin(Map<String, Object> map) throws Exception;

	/**
	 * 根据手机号查找用户
	 * @param confirmPhone
	 * @return
	 * @throws Exception
	 */
    User findByName(String confirmPhone) throws Exception;

	User selectByEamil(String email) throws Exception;

	void updateByPrimaryKeySelective(User u) throws Exception;

	User selectByPrimaryKey(Integer userId) throws Exception;

}
