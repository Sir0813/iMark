package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.msg.UserCardEnum;
import com.dm.user.service.CertFilesService;
import com.dm.user.service.UserCardService;
import com.dm.user.util.ShaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class UserCardServiceImpl implements UserCardService{

	@Autowired
	private UserCardMapper userCardMapper;

	@Autowired
	private CertFilesService certFilesService;

	@Override
	public Result authentication(UserCard userCard) throws Exception {
		try {
            UserCard uc = userCardMapper.selectByCardNumber(userCard.getCardNumber());
            if(null!=uc) {
                return ResultUtil.info("card.code.error.code","card.code.error.msg");
            }
			userCard.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
			userCard.setRealState(UserCardEnum.REAL_SUBMIT.getCode());
			userCard.setPostTime(new Date());
			// 实名认证成功
			if (UserCardEnum.REAL_SUCCESS.getCode().equals(userCard.getRealState())) {
				userCard.setRealTime(new Date());
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
			UserCard userCard = userCardMapper.selectByUserId(LoginUserHelper.getUserId());
			// 身份证号保密显示
			String cardNumber = userCard.getCardNumber();
			String starString = ShaUtil.getStarString(cardNumber, 1, cardNumber.length() - 1);
			userCard.setCardNumber(starString);
			return ResultUtil.success(userCard);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public UserCard selectByUserId(String toString) throws Exception {
		try {
			return userCardMapper.selectByUserId(toString);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
