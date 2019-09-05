package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;

import java.util.Map;

public interface InformationService {

	Result sendEmailCode(Map<String, Object> map) throws Exception;

	Result bindEmail(Map<String, Object> map) throws Exception;

	Result checkCode(Map<String, Object> map) throws Exception;

	Result changePhone(Map<String, Object> map) throws Exception;

}
