package com.dm.user.mapper;

import com.dm.user.entity.PwdHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface PwdHistoryMapper {

    /**
     * 删除
     *
     * @param key
     * @return
     */
    int deleteByPrimaryKey(PwdHistory key);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(PwdHistory record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(PwdHistory record);

    /**
     * 根据ID查询
     *
     * @param key
     * @return
     */
    PwdHistory selectByPrimaryKey(PwdHistory key);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PwdHistory record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(PwdHistory record);
}