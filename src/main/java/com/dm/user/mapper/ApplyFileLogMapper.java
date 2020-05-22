package com.dm.user.mapper;

import com.dm.user.entity.ApplyFileLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplyFileLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyFileLog record);

    int insertSelective(ApplyFileLog record);

    ApplyFileLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyFileLog record);

    int updateByPrimaryKeyWithBLOBs(ApplyFileLog record);

    int updateByPrimaryKey(ApplyFileLog record);

    List<ApplyFileLog> selectByFileId(int id);
}