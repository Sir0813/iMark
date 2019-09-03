package com.dm.user.mapper;

import com.dm.user.entity.CertFicate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CertFicateMapper {
    int deleteByPrimaryKey(Integer certId);

    int insert(CertFicate record);

    int insertSelective(CertFicate record);

    CertFicate selectByPrimaryKey(Integer certId);

    int updateByPrimaryKeySelective(CertFicate record);

    int updateByPrimaryKey(CertFicate record);

    List<CertFicate> list(Map<String, Object> map);

	List<CertFicate> selectByIDs(Integer[] ids);

	void updateByCertId(CertFicate certFicate);

	void updateCertRevoke(int certId);

	void updateReasonByCertId(int certId);

	void updateCertState(Map<String, Object> map);

    CertFicate selectByIdAndState(Integer certId);
}