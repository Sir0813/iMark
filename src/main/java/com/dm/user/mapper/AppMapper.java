package com.dm.user.mapper;

import com.dm.user.entity.App;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppMapper {
    int deleteByPrimaryKey(Integer appId);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Integer appId);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);

    App appVersion(App app);
}