package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFiles;
import com.dm.user.entity.User;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.CertFilesMapper;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.service.UserCardService;
import com.dm.user.util.ShaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserCardServiceImpl implements UserCardService{

	@Autowired
	private UserCardMapper userCardMapper;

	@Autowired
	private CertFilesMapper certFilesMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public Result authentication(UserCard userCard) throws Exception {
		try {
			String[] split = userCard.getFrontPath().split(",");
			CertFiles file = certFilesMapper.selectByPrimaryKey(Integer.parseInt(split[0]));
			CertFiles cf = certFilesMapper.selectByPrimaryKey(Integer.parseInt(split[1]));
			userCard.setFrontPath(file.getFileUrl());
			userCard.setBackPath(cf.getFileUrl());
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User u = userMapper.findByUserName(username);
			userCard.setUserid(u.getUserid());
			userCard.setRealState("1");
			userCard.setPostTime(new Date());
			if (userCard.getCardid()!=null) {
				userCardMapper.updateByPrimaryKeySelective(userCard);
			} else {
				userCardMapper.insertSelective(userCard);
			}
			return ResultUtil.success();
		} catch (NumberFormatException e) {
			throw new Exception(e);
		}
	}

	@Override
	public Result realInfo() throws Exception {
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User u = userMapper.findByUserName(username);
			UserCard userCard = userCardMapper.selectByUserId(u.getUserid().toString());
			userCard.setFrontPath("");
			userCard.setBackPath("");
			String cardNumber = userCard.getCardNumber();
			String starString = ShaUtil.getStarString(cardNumber, 1, cardNumber.length() - 1);
			userCard.setCardNumber(starString);
			return ResultUtil.success(userCard);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
