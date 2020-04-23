package com.dm.user.mapper;

import com.dm.user.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface UserMapper {

    /**
     * 删除
     *
     * @param userid
     * @return
     */
    int deleteByPrimaryKey(Integer userid);

    /**
     * 根据主键查询
     *
     * @param userid
     * @return
     */
    AppUser selectByPrimaryKey(Integer userid);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AppUser record);

    /**
     * 注册
     *
     * @param user
     */
    void userRegister(AppUser user);

    /**
     * 根据用户手机号查找
     *
     * @param username
     * @return
     */
    AppUser findByName(String username);

    /**
     * 根据ID修改密码
     *
     * @param map
     */
    void updateById(Map<String, Object> map);

    /**
     * 根据email查询
     *
     * @param email
     * @return
     */
    AppUser selectByEamil(String email);
}