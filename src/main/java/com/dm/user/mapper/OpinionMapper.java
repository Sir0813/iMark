package com.dm.user.mapper;

import com.dm.user.entity.Opinion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OpinionMapper {
    int deleteByPrimaryKey(Integer opinionId);

    int insert(Opinion record);

    int insertSelective(Opinion record);

    Opinion selectByPrimaryKey(Integer opinionId);

    int updateByPrimaryKeySelective(Opinion record);

    int updateByPrimaryKey(Opinion record);

}