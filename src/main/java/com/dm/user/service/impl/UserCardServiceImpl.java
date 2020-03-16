package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.msg.UserCardEnum;
import com.dm.user.service.UserCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCardServiceImpl implements UserCardService {

    @Autowired
    private UserCardMapper userCardMapper;

    @Override
    public Result authentication(UserCard userCard) throws Exception {
        try {
            UserCard uc = userCardMapper.selectByCardNumber(userCard.getCardNumber());
            if (null != uc) {
                return ResultUtil.info("card.code.error.code", "card.code.error.msg");
            }
            // 实名认证成功
            if (userCard.getCardid() != null) {
                if (UserCardEnum.REAL_SUCCESS.getCode().equals(userCard.getRealState())) {
                    userCard.setRealTime(new Date());
                }
                userCardMapper.updateByPrimaryKeySelective(userCard);
            } else {
                userCard.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
                userCard.setPostTime(new Date());
                userCardMapper.insertSelective(userCard);
            }
            return ResultUtil.success(userCard.getCardid());
        } catch (NumberFormatException e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result realInfo() throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("userid", LoginUserHelper.getUserId());
            UserCard userCard = userCardMapper.selectByUserId(map);
            if (null == userCard) {
                return ResultUtil.success();
            }
            // 身份证号保密显示
			/*String cardNumber = userCard.getCardNumber();
			String starString = ShaUtil.getStarString(cardNumber, 1, cardNumber.length() - 1);
			userCard.setCardNumber(starString);*/
            return ResultUtil.success(userCard);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public UserCard selectByUserId(String toString, String realState) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("userid", LoginUserHelper.getUserId());
            map.put("realState", realState);
            return userCardMapper.selectByUserId(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void updateRealState() throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("userid", LoginUserHelper.getUserId());
            UserCard userCard = userCardMapper.selectByUserId(map);
            if (null != userCard) {
                userCard.setRealState(UserCardEnum.NOT_REAL.getCode());
                userCard.setRealTime(null);
                userCardMapper.updateByPrimaryKey(userCard);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
