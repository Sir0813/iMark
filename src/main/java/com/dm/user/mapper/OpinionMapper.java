package com.dm.user.mapper;

import com.dm.user.entity.Opinion;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface OpinionMapper {

    /**
     * 删除
     *
     * @param opinionId
     * @return
     */
    int deleteByPrimaryKey(Integer opinionId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(Opinion record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(Opinion record);

    /**
     * 根据ID查询
     *
     * @param opinionId
     * @return
     */
    Opinion selectByPrimaryKey(Integer opinionId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Opinion record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Opinion record);

}