package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.User;

import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface UserService {

	Result userRegister(User user) throws Exception;

	Result resetPassword(Map<String,Object>map) throws Exception;

	Result sendVeriCode(String phone) throws Exception;

	Result userInfo() throws Exception;

	Result getPushMsg() throws Exception;

	String getRegistrationId(Map<String, Object> map) throws Exception;

	Result userUpdate(User user)throws Exception;

    Result retrievePwd(Map<String, Object> map) throws Exception;

	Result nextOperate(Map<String, Object> map) throws Exception;

    Result dynamicLogin(Map<String, Object> map) throws Exception;

    User findByName(String confirmPhone) throws Exception;

	User selectByEamil(String email) throws Exception;

	void updateByPrimaryKeySelective(User u) throws Exception;

	User selectByPrimaryKey(Integer userId) throws Exception;

}
