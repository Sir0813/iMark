package com.dm.user.service.impl;

import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.security.LoginUserDetailsService;
import com.dm.frame.jboot.security.MD5PasswordEncoder;
import com.dm.frame.jboot.security.token.JwtTokenProvider;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.frame.jboot.util.MD5Util;
import com.dm.user.entity.*;
import com.dm.user.mapper.CertConfirmMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

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

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private LoginUserDetailsService loginUserDetailsService;

	@Autowired
	private CertConfirmMapper certConfirmMapper;

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
				return ResultUtil.info("register.no.name.code","register.no.name.msg");
			}
			if (StringUtils.isBlank(user.getPassword())) {
				return ResultUtil.info("register.no.password.code","register.no.password.msg");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("email", user.getUsername());
			map.put("veriCode", user.getUsercode());
			Information information = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(information,map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			User u = userMapper.findByName(user.getUsername());
			if (null!=u) {
				return ResultUtil.info("register.has.name.code","register.has.name.msg");
			}
			information.setInfoState("1");
			user.setSalt(RandomCode.getCode());
			user.setUsercode("");
			String md5Password = MD5Util.encode(user.getPassword()+user.getSalt());
			user.setPassword(md5Password);
			user.setCreatedDate(DateUtil.getSystemDateStr());
			informationMapper.updateByPrimaryKeySelective(information);
			userMapper.userRegister(user);
			/*待自己确认 需要更新注册用户ID*/
			List<CertConfirm>list = certConfirmMapper.selectByPhone(user.getUsername());
			if (list.size()>0){
				list.forEach(l->{
					l.setUserId(user.getUserid());
					certConfirmMapper.updateByPrimaryKeySelective(l);
				});
			}
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
				return ResultUtil.info("user.reset.password.error.code","user.reset.password.error.msg");
			}
			String newPwd = MD5Util.encode(map.get("newPassword").toString()+user.getSalt());
			if (newPwd.equals(user.getPassword())) {
				return ResultUtil.info("user.reset.equal.password.code","user.reset.equal.password.msg");
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
			User user = userMapper.findByName(username);
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
			User user = userMapper.findByName(username);
			user.setUsercode(registrationId);
			userMapper.updateByPrimaryKeySelective(user);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private Result checkVeriCode(Information info,Map<String, Object> map) throws Exception {
		try {
			if (null==info||!map.get("veriCode").toString().equals(info.getInfoCode())) {
				return ResultUtil.info("email.code.error.code","email.code.error.msg");
			}
			Date date = new Date();
			if (date.after(info.getInfoExpireddate())) {
				return ResultUtil.info("email.code.expire.code","email.code.expire.msg");
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

	@Override
	public Result retrievePwd(Map<String, Object> map) throws Exception {
		try {
			User user = userMapper.findByName(map.get("phone").toString());
			user.setSalt(RandomCode.getCode());
			String md5Password = MD5Util.encode(map.get("password").toString()+user.getSalt());
			user.setPassword(md5Password);
			userMapper.updateByPrimaryKeySelective(user);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Result nextOperate(Map<String, Object> map) throws Exception {
		try {
			User user = userMapper.findByName(map.get("phone").toString());
			if (null==user) {
				return ResultUtil.info("login.account.no.code","login.account.no.msg");
			}
			map.put("email", map.get("phone").toString());
			map.put("veriCode", map.get("veriCode").toString());
			Information information = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(information, map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			information.setInfoState("1");
			informationMapper.updateByPrimaryKeySelective(information);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Result dynamicLogin(Map<String, Object> map) throws Exception {
		User user = userMapper.findByName(map.get("username").toString());
		if (null==user) {
			return ResultUtil.info("login.account.no.code","login.account.no.msg");
		}
		map.put("email", map.get("username").toString());
		map.put("veriCode", map.get("password").toString());
		Information information = informationMapper.selectByPhone(map);
		Result result = checkVeriCode(information, map);
		if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
			return result;
		}
		MD5PasswordEncoder md5PasswordEncoder = new MD5PasswordEncoder();
		String encodePwd = md5PasswordEncoder.encode(map.get("password").toString());
		Collection<GrantedAuthority> authorities = this.loginUserDetailsService.loadAuthority(map.get("username").toString());
		Authentication authentication = new UsernamePasswordAuthenticationToken(map.get("username").toString(), encodePwd, authorities);
		String token = this.jwtTokenProvider.createToken(authentication);
		information.setInfoState("1");
		informationMapper.updateByPrimaryKeySelective(information);
		return ResultUtil.success(token);
	}
}
