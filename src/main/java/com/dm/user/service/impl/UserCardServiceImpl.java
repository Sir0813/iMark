package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.CertFiles;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.service.CertFilesService;
import com.dm.user.service.UserCardService;
import com.dm.user.util.ShaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
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
			boolean b = userCard.getCardid()!=null?true:false;
			if(b){
				UserCard card = userCardMapper.selectByPrimaryKey(userCard.getCardid());
				CertFiles c = certFilesService.selectByUrl(card.getFrontPath());
				CertFiles certFiles = certFilesService.selectByUrl(card.getBackPath());
				File file = new File(c.getFilePath());
				File f = new File(certFiles.getFilePath());
				file.delete();
				f.delete();
				certFilesService.deleteByPrimaryKey(c.getFileId());
				certFilesService.deleteByPrimaryKey(certFiles.getFileId());
			}
			String[] split = userCard.getFrontPath().split(",");
			CertFiles file = certFilesService.selectByPrimaryKey(Integer.parseInt(split[0]));
			CertFiles cf = certFilesService.selectByPrimaryKey(Integer.parseInt(split[1]));
			userCard.setFrontPath(file.getFileUrl());
			userCard.setBackPath(cf.getFileUrl());
			userCard.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
			userCard.setRealState("1");
			userCard.setPostTime(new Date());
			if (b) {
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

	@Override
	public UserCard selectByUserId(String toString) throws Exception {
		try {
			return userCardMapper.selectByUserId(toString);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
