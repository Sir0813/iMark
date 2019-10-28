package com.dm.user.mapper;

import com.dm.user.entity.UserCard;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface UserCardMapper {

    /**
     * 删除
     *
     * @param cardid
     * @return
     */
    int deleteByPrimaryKey(Integer cardid);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(UserCard record);

    /**
     * 根据ID查询
     *
     * @param cardid
     * @return
     */
    UserCard selectByPrimaryKey(Integer cardid);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserCard record);

    /**
     * 根据用户ID查询
     *
     * @param userid
     * @return
     */
    UserCard selectByUserId(String userid, String realState);

    /**
     * 根据身份证号查询
     *
     * @param cardNumber
     * @return
     */
    UserCard selectByCardNumber(String cardNumber);
}