package com.dm.user.service.impl;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.Information;
import com.dm.user.entity.User;
import com.dm.user.mapper.InformationMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.InformationService;
import com.dm.user.util.RandomCode;
import com.dm.user.util.SendMailSmtp;

@Service
@Transactional(rollbackFor=Exception.class)
public class InformationServiceImpl implements InformationService{

	@Autowired
	private InformationMapper informationMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SendMailSmtp sendMailSmtp;
	
	@Value("${email.subject}")
	private String subject;
	
	@Value("${email.emailContent}")
	private String emailContent;
	
	@Value("${email.expired}")
	private long expired;
	
	@Override
	public Result sendEmailCode(Map<String, Object> map) throws Exception {
		try {
			Information info = new Information();
			long randomNumber = RandomCode.generateRandomNumber(6);
			String replaceContent = emailContent.replace("$code$", String.valueOf(randomNumber));
			boolean sendEmail = sendMailSmtp.sendEmail(map.get("email").toString(), replaceContent, subject);
			if (!sendEmail) {
				return ResultUtil.error();
			}
			Date date = new Date();
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			info.setInfoCode(String.valueOf(randomNumber));
			info.setInfoMsg(replaceContent);
			info.setInfoPhone(map.get("email").toString());
			info.setInfoSenddate(date);
			info.setInfoUser(username);
			info.setInfoExpireddate(new Date(date.getTime()+expired));
			info.setInfoState("0");
			informationMapper.insertSelective(info);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public Result bindEmail(Map<String, Object> map) throws Exception {
		Information info = informationMapper.selectByPhone(map);
		Result result = checkVeriCode(info,map);
		if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
			return result;
		}
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User u = userMapper.findByUserName(username);
		u.setEmail(map.get("email").toString());
		userMapper.updateByPrimaryKeySelective(u);
		info.setInfoState("1");
		informationMapper.updateByPrimaryKeySelective(info);
		return ResultUtil.success();
	}
	
	@Override
	public Result checkCode(Map<String, Object> map) throws Exception {
		map.put("email", map.get("oldPhone").toString());
		Information info = informationMapper.selectByPhone(map);
		return checkVeriCode(info,map);
	}
	
	@Override
	public Result changePhone(Map<String, Object> map) throws Exception {
		map.put("email", map.get("newPhone").toString());
		User user = userMapper.findByUserName(map.get("newPhone").toString());
		if (null!=user) {
			return ResultUtil.info(I18nUtil.getMessage("register.has.name.code"),
					I18nUtil.getMessage("register.has.name.msg"));
		}
		Information info = informationMapper.selectByPhone(map);
		Result result = checkVeriCode(info,map);
		if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
			return result;
		}
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User u = userMapper.findByUserName(username);
		u.setUsername(map.get("newPhone").toString());
		userMapper.updateByPrimaryKeySelective(u);
		return ResultUtil.success();
	}
	
	public Result checkVeriCode(Information info,Map<String, Object> map) {
		if (null==info||!map.get("veriCode").toString().equals(info.getInfoCode())) {
			return ResultUtil.info(I18nUtil.getMessage("email.code.error.code"),
						I18nUtil.getMessage("email.code.error.msg"));
		}
		Date date = new Date();
		if (date.after(info.getInfoExpireddate())) {
			return ResultUtil.info(I18nUtil.getMessage("email.code.expire.code"),
					I18nUtil.getMessage("email.code.expire.msg"));
		}
		return ResultUtil.success();
	}
}
