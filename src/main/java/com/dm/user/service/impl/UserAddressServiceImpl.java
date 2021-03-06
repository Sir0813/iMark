package com.dm.user.service.impl;

import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.UserAddress;
import com.dm.user.mapper.UserAddressMapper;
import com.dm.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> list(String userId) throws Exception {
        return userAddressMapper.list(userId);
    }

    @Override
    public void insert(UserAddress userAddress) throws Exception {
        if (userAddress.getIsDefault() == 1) {
            userAddressMapper.updateIsDefault(LoginUserHelper.getUserId());
        }
        if (null != userAddress.getId()) {
            userAddress.setUpdateDate(new Date());
            userAddressMapper.updateByPrimaryKeySelective(userAddress);
        } else {
            userAddress.setCreatedDate(new Date());
            userAddress.setUserId(Integer.parseInt(LoginUserHelper.getUserId()));
            userAddressMapper.insertSelective(userAddress);
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        userAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UserAddress selectByUserIdAndStatus(String userId, String status) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("userId", userId);
        map.put("status", status);
        return userAddressMapper.selectByUserIdAndStatus(map);
    }

    @Override
    public UserAddress selectById(Integer addressId) throws Exception {
        return userAddressMapper.selectByPrimaryKey(addressId);
    }
}
