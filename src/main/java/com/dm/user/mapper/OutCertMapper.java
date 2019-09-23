package com.dm.user.mapper;

import com.dm.user.entity.OutCert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutCertMapper {
    int deleteByPrimaryKey(Integer outCertId);

    int insert(OutCert record);

    int insertSelective(OutCert record);

    OutCert selectByPrimaryKey(Integer outCertId);

    int updateByPrimaryKeySelective(OutCert record);

    int updateByPrimaryKey(OutCert record);

    List<OutCert> list(String userId);
}