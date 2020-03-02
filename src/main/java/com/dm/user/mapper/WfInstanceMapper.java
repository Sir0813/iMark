package com.dm.user.mapper;

import com.dm.user.entity.WfInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface WfInstanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(WfInstance record);

    WfInstance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WfInstance record);

    void updateByInstanceId(Map<String, Object> map);
}