package com.dm.user.mapper;

import com.dm.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	void userRegister(User user);

	User findByName(String username);

	void updateById(Map<String, Object> map);

    User userData();

    User selectByEamil(String email);
}