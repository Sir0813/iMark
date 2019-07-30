package com.dm.user.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.dm.user.entity.User;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	void userRegister(User user);

	User findByUserName(String username);

	void updateById(Map<String, Object> map);
}