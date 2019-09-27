package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.Information;

import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface InformationService {

	/**
	 * 发送邮箱验证码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Result sendEmailCode(Map<String, Object> map) throws Exception;

	/**
	 * 绑定邮箱
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Result bindEmail(Map<String, Object> map) throws Exception;

	/**
	 * 校验验证码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Result checkCode(Map<String, Object> map) throws Exception;

	/**
	 * 修改手机号
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Result changePhone(Map<String, Object> map) throws Exception;

	/**
	 * 新增
	 * @param info
	 * @throws Exception
	 */
    void insertSelective(Information info) throws Exception;

	/**
	 * 根据手机号查询
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Information selectByPhone(Map<String, Object> map) throws Exception;

	/**
	 * 修改
	 * @param information
	 * @throws Exception
	 */
	void updateByPrimaryKeySelective(Information information) throws Exception;
}
