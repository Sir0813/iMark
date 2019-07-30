package com.dm.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.dm.user.entity.CertFiles;

@Mapper
public interface CertFilesMapper {
    int deleteByPrimaryKey(Integer fileId);

    int insertSelective(CertFiles record);

    CertFiles selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(CertFiles record);

	List<CertFiles> findByFilesIds(String[] filesId);
}