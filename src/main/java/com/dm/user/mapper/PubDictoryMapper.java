package com.dm.user.mapper;

import com.dm.user.entity.PubDictory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PubDictoryMapper {

    PubDictory selectByPrimaryKey(Integer id);

    List<PubDictory> selectCountry(String country);

    List<PubDictory> selectCountryLanguage(Integer id);
}