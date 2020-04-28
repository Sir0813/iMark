package com.dm.user.mapper;

import com.dm.user.entity.ApplyExpand;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyExpandMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ApplyExpand record);

    ApplyExpand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyExpand record);

    ApplyExpand selectByApplyId(int applyid);
}