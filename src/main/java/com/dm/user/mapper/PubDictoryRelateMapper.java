package com.dm.user.mapper;

import com.dm.user.entity.PubDictoryRelate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PubDictoryRelateMapper {

    PubDictoryRelate selectByPrimaryKey(Integer id);

}