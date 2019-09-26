package com.dm.user.service.impl;

import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.*;
import com.dm.user.mapper.ContactMapper;
import com.dm.user.mapper.InformationMapper;
import com.dm.user.service.CertConfirmService;
import com.dm.user.service.InformationService;
import com.dm.user.service.PushMsgService;
import com.dm.user.service.UserService;
import com.dm.user.util.RandomCode;
import com.dm.user.util.SendMailSmtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class InformationServiceImpl<insertSelective> implements InformationService{

	@Autowired
	private InformationMapper informationMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SendMailSmtp sendMailSmtp;

	@Autowired
	private CertConfirmService certConfirmService;

	@Autowired
	private PushMsgService pushMsgService;

	@Autowired
	private ContactMapper contactMapper;
	
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
			info.setInfoCode(String.valueOf(randomNumber));
			info.setInfoMsg(replaceContent);
			info.setInfoPhone(map.get("email").toString());
			info.setInfoSenddate(date);
			info.setInfoUser(LoginUserHelper.getUserName());
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
		try {
			Information info = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(info,map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			User user = userService.selectByEamil(map.get("email").toString());
			if (null!=user){
				return ResultUtil.info("email.code.have.code","email.code.have.msg");
			}
			User u = userService.findByName(LoginUserHelper.getUserName());
			u.setEmail(map.get("email").toString());
			userService.updateByPrimaryKeySelective(u);
			info.setInfoState("1");
			informationMapper.updateByPrimaryKeySelective(info);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public Result checkCode(Map<String, Object> map) throws Exception {
		try {
			map.put("email", map.get("oldPhone").toString());
			Information info = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(info,map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			info.setInfoState("1");
			informationMapper.updateByPrimaryKeySelective(info);
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public Result changePhone(Map<String, Object> map) throws Exception {
		try {
			User user = userService.findByName(map.get("newPhone").toString());
			if (null!=user) {
				return ResultUtil.info("register.has.name.code","register.has.name.msg");
			}
			map.put("email", map.get("newPhone").toString());
			Information info = informationMapper.selectByPhone(map);
			Result result = checkVeriCode(info,map);
			if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
				return result;
			}
			User u = userService.findByName(LoginUserHelper.getUserName());
			u.setUsername(map.get("newPhone").toString());
			userService.updateByPrimaryKeySelective(u);
			info.setInfoState("1");
			informationMapper.updateByPrimaryKeySelective(info);
			/*待自己确认 需要更新注册用户ID*/
			List<CertConfirm>list = certConfirmService.selectByPhone(u.getUsername());
			if (list.size()>0){
				list.forEach(l->{
					l.setUserId(u.getUserid());
					certConfirmService.updateByPrimaryKeySelective(l);
				});
			}
			/*出证发送给我的添加用户ID*/
			List<Contact> contacts = contactMapper.selectByPhone(u.getUsername());
			if (contacts.size()>0){
				contacts.forEach(contact -> {
					contact.setUserId(u.getUserid());
					contactMapper.updateByPrimaryKey(contact);
				});
			}
			/*历史消息添加用户ID*/
			List<PushMsg> pushMsgs = pushMsgService.selectByReceiveAndState(u.getUsername());
			if (pushMsgs.size()>0){
				for (int i = 0; i < pushMsgs.size(); i++) {
					PushMsg pushMsg =  pushMsgs.get(i);
					pushMsg.setUserId(u.getUserid());
					pushMsgService.updateByPrimaryKeySelective(pushMsg);
				}
			}
			return ResultUtil.success();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void insertSelective(Information info) throws Exception {
		try {
			informationMapper.insertSelective(info);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Information selectByPhone(Map<String, Object> map) throws Exception {
		try {
			return informationMapper.selectByPhone(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void updateByPrimaryKeySelective(Information information) throws Exception {
		try {
			informationMapper.updateByPrimaryKeySelective(information);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private Result checkVeriCode(Information info, Map<String, Object> map) throws Exception {
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
}
