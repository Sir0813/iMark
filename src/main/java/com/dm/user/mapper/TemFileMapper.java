package com.dm.user.mapper;

import com.dm.user.entity.TemFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemFileMapper {
    int deleteByPrimaryKey(Integer temId);

    int insert(TemFile record);

    int insertSelective(TemFile record);

    TemFile selectByPrimaryKey(Integer temId);

    int updateByPrimaryKeySelective(TemFile record);

    TemFile selectByCertId(String certId);
}