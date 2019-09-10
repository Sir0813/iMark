package com.dm.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.dm.user.entity.UserCard;

@Mapper
public interface UserCardMapper {
    int deleteByPrimaryKey(Integer cardid);

    int insertSelective(UserCard record);

    UserCard selectByPrimaryKey(Integer cardid);

    int updateByPrimaryKeySelective(UserCard record);

	UserCard selectByUserId(String userid);

    UserCard selectByCardNumber(String cardNumber);
}