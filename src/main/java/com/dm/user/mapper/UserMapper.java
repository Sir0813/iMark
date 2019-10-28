package com.dm.user.mapper;

import com.dm.user.entity.User;
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
     * 新增
     *
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据主键查询
     *
     * @param userid
     * @return
     */
    User selectByPrimaryKey(Integer userid);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);

    /**
     * 注册
     *
     * @param user
     */
    void userRegister(User user);

    /**
     * 根据用户手机号查找
     *
     * @param username
     * @return
     */
    User findByName(String username);

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
    User selectByEamil(String email);
}