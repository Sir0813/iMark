package com.dm.user.mapper;

import com.dm.user.entity.BizItemVideo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BizItemVideoMapper {

    int insertSelective(BizItemVideo record);

    BizItemVideo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BizItemVideo record);

    List<BizItemVideo> selectByApplyId(int applyid);
}