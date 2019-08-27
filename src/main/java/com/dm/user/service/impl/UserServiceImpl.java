package com.dm.user.service.impl;

import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.frame.jboot.util.MD5Util;
import com.dm.user.entity.Information;
import com.dm.user.entity.PushMsg;
import com.dm.user.entity.User;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.InformationMapper;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.PushMsgService;
import com.dm.user.service.UserService;
import com.dm.user.util.PushUtil;
import com.dm.user.util.RandomCode;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
    private LoginUserService loginUserService;
	
	@Autowired
	private InformationMapper informationMapper;
	
	@Autowired
	private UserCardMapper userCardMapper;

	@Autowired
	private PushMsgService pushMsgService;
	
	@Value("${email.emailContent}")
	private String emailContent;
	
	@Value("${email.expired}")
	private long expired;
	
	@Override
	public String sendVeriCode(String phone) throws Exception {
		try {
			Information info = new Information();
			Date date = new Date();
			long randomNumber = RandomCode.generateRandomNumber(6);
			String replaceContent = emailContent.replace("$code$", String.valueOf(randomNumber));
			info.setInfoCode(String.valueOf(randomNumber));
			info.setInfoMsg(replaceContent);
			info.setInfoPhone(phone);
			info.setInfoSenddate(date);
			info.setInfoExpireddate(new Date(date.getTime()+expired));
			info.setInfoState("0");
			informationMapper.insertSelective(info);
			return String.valueOf(randomNumber);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public Result userRegister(User user) throws Exception {
		try {
			if (StringUtils.isBlank(user.getUsername())) {
				return ResultUtil.info(I18nUtil.getMessage("register.no.name.code"),
						I18nUtil.getMessage("register.no.name.msg"));
			}
			if (StringUtils.isBlank(user.getPassword())) {
				return ResultUtil.info(I18nUtil.getMessage("register.no.password.code"),
						I18nUtil.getMessage("register.no.password.msg"));
			}
			Map<String, Object> map = new HashMap<>();
			map.put("email", user.getUsername());
			map.put("veriCode", user.getUsercode());
			Information information = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(information,map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			User u = userMapper.findByUserName(user.getUsername());
			if (null!=u) {
				return ResultUtil.info(I18nUtil.getMessage("register.has.name.code"),
						I18nUtil.getMessage("register.has.name.msg"));
			}
			information.setInfoState("1");
			user.setSalt(RandomCode.getCode());
			user.setUsercode("");
			String md5Password = MD5Util.encode(user.getPassword()+user.getSalt());
			user.setPassword(md5Password);
			user.setCreatedDate(DateUtil.getSystemDateStr());
			informationMapper.updateByPrimaryKeySelective(information);
			userMapper.userRegister(user);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	@Override
	public Result resetPassword(Map<String,Object>map) throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			LoginUserDetails user = loginUserService.getUserByUsername(username);
			String md5Password = MD5Util.encode(map.get("oldPassword").toString()+user.getSalt());
			if (!md5Password.equals(user.getPassword())) {
				return ResultUtil.info(I18nUtil.getMessage("user.reset.password.error.code"),
						I18nUtil.getMessage("user.reset.password.error.msg"));
			}
			String newPwd = MD5Util.encode(map.get("newPassword").toString()+user.getSalt());
			if (newPwd.equals(user.getPassword())) {
				return ResultUtil.info(I18nUtil.getMessage("user.reset.equal.password.code"),
						I18nUtil.getMessage("user.reset.equal.password.msg"));
			}else {
				map.put("userid", user.getUserid());
				map.put("password", newPwd);
				userMapper.updateById(map);
			}
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public Result userInfo() throws Exception {
		try {
			Map<String,Object> map = new HashMap<>();
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userMapper.findByUserName(username);
			UserCard userCard = userCardMapper.selectByUserId(user.getUserid().toString());
			map.put("email", StringUtils.isBlank(user.getEmail())?"":user.getEmail());
			map.put("userName", username);
			map.put("realName", null==userCard?"":userCard.getRealName());
			map.put("headPhoto", StringUtils.isBlank(user.getHeadPhoto())?"":user.getHeadPhoto());
			map.put("realState", null==userCard?"":userCard.getRealState());
			map.put("sex", user.getSex());
			map.put("nickName", null==user.getDescribe()?"":user.getDescribe());
			map.put("userid", user.getUserid());
			return ResultUtil.success(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Result getPushMsg() throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<PushMsg> pmList = pushMsgService.selectByReceiveAndState(username);
			if (pmList.size()>0){
				pmList.forEach(pm->{
					String json = new Gson().toJson(pm);
					try {
						int resout = PushUtil.getInstance().sendToRegistrationId(username, pm.getTitle(), json);
						if (resout==1){
							pm.setState("1");
							pushMsgService.updateByPrimaryKeySelective(pm);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void getRegistrationId(Map<String,Object>map) throws Exception {
		try {
			String registrationId = map.get("registrationId").toString();
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userMapper.findByUserName(username);
			user.setUsercode(registrationId);
			userMapper.updateByPrimaryKeySelective(user);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public Result checkVeriCode(Information info,Map<String, Object> map) throws Exception {
		try {
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
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Result userUpdate(User user) throws Exception {
		userMapper.updateByPrimaryKeySelective(user);
		return ResultUtil.success();
	}
}
