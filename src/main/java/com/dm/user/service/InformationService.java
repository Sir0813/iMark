package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.Information;

import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface InformationService {

	Result sendEmailCode(Map<String, Object> map) throws Exception;

	Result bindEmail(Map<String, Object> map) throws Exception;

	Result checkCode(Map<String, Object> map) throws Exception;

	Result changePhone(Map<String, Object> map) throws Exception;

    void insertSelective(Information info) throws Exception;

	Information selectByPhone(Map<String, Object> map) throws Exception;

	void updateByPrimaryKeySelective(Information information) throws Exception;
}
