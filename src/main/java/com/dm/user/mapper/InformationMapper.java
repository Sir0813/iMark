package com.dm.user.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.dm.user.entity.Information;

@Mapper
public interface InformationMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(Information record);

    int insertSelective(Information record);

    Information selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(Information record);

    int updateByPrimaryKey(Information record);

	Information selectByPhone(Map<String, Object> map);
}