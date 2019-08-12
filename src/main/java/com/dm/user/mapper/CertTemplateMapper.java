package com.dm.user.mapper;

import com.dm.user.entity.CertTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CertTemplateMapper {
    int deleteByPrimaryKey(Integer templateId);

    int insertSelective(CertTemplate record);

    CertTemplate selectByPrimaryKey(Integer templateId);

    int updateByPrimaryKeySelective(CertTemplate record);

    List<CertTemplate> list();

    CertTemplate getByTemplateType(String type);
}