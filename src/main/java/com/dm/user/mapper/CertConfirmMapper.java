package com.dm.user.mapper;

import com.dm.user.entity.CertConfirm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CertConfirmMapper {
    int deleteByPrimaryKey(Integer confirmId);

    int insertSelective(CertConfirm record);

    CertConfirm selectByPrimaryKey(Integer confirmId);

    int updateByPrimaryKeySelective(CertConfirm record);

	List<CertConfirm> selectByCertId(Integer certFicateId);

	List<CertConfirm> selectByConfirmPhoneAndState(String username);

	void updateByCertId(Map<String, Object> map);

	void updateConfirmState(Map<String, Object> map);

	List<CertConfirm> selectByState(Map<String, Object> map);

	List<CertConfirm> selectByConfirmPhone(String username);

    void deleteByCertId(Integer certId);
}