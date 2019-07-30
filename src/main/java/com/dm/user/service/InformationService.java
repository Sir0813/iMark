package com.dm.user.service;

import java.util.Map;
import com.dm.frame.jboot.msg.Result;

public interface InformationService {

	Result sendEmailCode(Map<String, Object> map) throws Exception;

	Result bindEmail(Map<String, Object> map) throws Exception;

	Result checkCode(Map<String, Object> map) throws Exception;

	Result changePhone(Map<String, Object> map) throws Exception;

}
