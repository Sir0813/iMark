package com.dm.user.mapper;

import com.dm.user.entity.Contact;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface ContactMapper {

    /**
     * 删除
     * @param contactId
     * @return
     */
    int deleteByPrimaryKey(Integer contactId);

    /**
     * 新增
     * @param record
     * @return
     */
    int insert(Contact record);

    /**
     * 新增
     * @param record
     * @return
     */
    int insertSelective(Contact record);

    /**
     * 根据ID查询
     * @param contactId
     * @return
     */
    Contact selectByPrimaryKey(Integer contactId);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Contact record);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(Contact record);

    /**
     * 根据用户ID查询
     * @param userId
     * @return
     */
    List<Contact> selectByUserId(String userId);

    /**
     * 根据用户名称查询
     * @param username
     * @return
     */
    List<Contact> selectByPhone(String username);

}