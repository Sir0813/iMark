package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.AppUser;
import com.dm.user.entity.UserCard;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.msg.UserCardEnum;
import com.dm.user.service.UserCardService;
import com.dm.user.service.UserService;
import com.dm.user.util.TidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    private UserService userService;

    @Autowired
    private TidUtil tidUtil;

    @Override
    public Result authentication(UserCard userCard) throws Exception {
        AppUser appUser = userService.selectByPrimaryKey(Integer.parseInt(LoginUserHelper.getUserId()));
        if (userCard.getCardid() != null) {
            if (UserCardEnum.REAL_SUCCESS.getCode().equals(userCard.getRealState())) {
                String code = tidUtil.checkTid(LoginUserHelper.getUserName(), userCard.getCardNumber());
                if ("201".equals(code)) {
                    if (!tidUtil.queryTid(userCard.getCardNumber())) {
                        tidUtil.addTid(userCard.getCardNumber());
                    }
                    tidUtil.addBind(LoginUserHelper.getUserName(), appUser.getPassword(), userCard.getCardNumber());
                } else if (!"200".equals(code)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultUtil.error();
                }
                userCard.setRealTime(new Date());
            }
            userCardMapper.updateByPrimaryKeySelective(userCard);
        } else {
            userCard.setUserid(Integer.parseInt(LoginUserHelper.getUserId()));
            userCard.setPostTime(new Date());
            userCardMapper.insertSelective(userCard);
        }
        return ResultUtil.success(userCard.getCardid());
    }

    @Override
    public Result realInfo() throws Exception {
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
    }

    @Override
    public UserCard selectByUserId(String userId, String realState) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userid", userId);
        map.put("realState", realState);
        return userCardMapper.selectByUserId(map);
    }

    @Override
    public void updateRealState() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("userid", LoginUserHelper.getUserId());
        UserCard userCard = userCardMapper.selectByUserId(map);
        if (null != userCard) {
            if (userCard.getRealState().equals(UserCardEnum.REAL_SUCCESS.getCode())) {
                userCard.setRealState(UserCardEnum.IS_REAL_SUCCESS.getCode());
                userCard.setRealTime(null);
            } else {
                return;
            }
        }
        userCardMapper.updateByPrimaryKey(userCard);
    }

    @Override
    public UserCard selectByUserIdAndStatus(Integer userid) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", userid);
        map.put("realSuccess", UserCardEnum.REAL_SUCCESS.getCode());
        map.put("isReal", UserCardEnum.IS_REAL_SUCCESS.getCode());
        UserCard userCard = userCardMapper.selectByUserIdAndStatus(map);
        return userCard;
    }
}
