package com.dm.user.mapper;

import com.dm.user.entity.ApplySupplement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplySupplementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplySupplement record);

    int insertSelective(ApplySupplement record);

    ApplySupplement selectByPrimaryKey(Integer id);

    ApplySupplement selectByApplyId(@Param("applyId") Integer applyId);

    int updateByPrimaryKeySelective(ApplySupplement record);

    int updateByPrimaryKey(ApplySupplement record);

}
