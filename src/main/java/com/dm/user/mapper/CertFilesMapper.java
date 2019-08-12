package com.dm.user.mapper;

import com.dm.user.entity.CertFiles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CertFilesMapper {
    int deleteByPrimaryKey(Integer fileId);

    int insertSelective(CertFiles record);

    CertFiles selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(CertFiles record);

	List<CertFiles> findByFilesIds(String[] filesId);

    void deleteByCertId(Integer certId);
}