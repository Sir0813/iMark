package com.dm.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.dm.user.entity.PwdHistory;

@Mapper
public interface PwdHistoryMapper {
    int deleteByPrimaryKey(PwdHistory key);

    int insert(PwdHistory record);

    int insertSelective(PwdHistory record);

    PwdHistory selectByPrimaryKey(PwdHistory key);

    int updateByPrimaryKeySelective(PwdHistory record);

    int updateByPrimaryKey(PwdHistory record);
}