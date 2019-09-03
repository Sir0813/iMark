package com.dm.user.mapper;

import com.dm.user.entity.CertVerify;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CertVerifyMapper {
    int deleteByPrimaryKey(Integer verifyId);

    int insertSelective(CertVerify record);

    CertVerify selectByPrimaryKey(Integer verifyId);

    int updateByPrimaryKeySelective(CertVerify record);

}