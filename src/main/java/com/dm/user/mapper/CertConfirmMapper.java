package com.dm.user.mapper;

import com.dm.user.entity.CertConfirm;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface CertConfirmMapper {

	/**
	 * 根据ID删除
	 * @param confirmId
	 * @return
	 */
    int deleteByPrimaryKey(Integer confirmId);

	/**
	 * 新增
	 * @param record
	 * @return
	 */
	int insertSelective(CertConfirm record);

	/**
	 * 根据ID查询
	 * @param confirmId
	 * @return
	 */
    CertConfirm selectByPrimaryKey(Integer confirmId);

	/**
	 * 修改
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(CertConfirm record);

	/**
	 * 根据存证ID查询
	 * @param certFicateId
	 * @return
	 */
	List<CertConfirm> selectByCertId(Integer certFicateId);

	/**
	 * 根据用户ID和状态查询
	 * @param userId
	 * @param state
	 * @return
	 */
	List<CertConfirm> selectByuserIdAndState(String userId,String state);

	/**
	 * 修改
	 * @param map
	 */
	void updateByCertId(Map<String, Object> map);

	/**
	 * 修改
	 * @param map
	 */
	void updateConfirmState(Map<String, Object> map);

	/**
	 * 根据状态查询
	 * @param map
	 * @return
	 */
	List<CertConfirm> selectByState(Map<String, Object> map);

	/**
	 * 根据用户ID查询
	 * @param userId
	 * @return
	 */
	List<CertConfirm> selectByUserId(Integer userId);

	/**
	 * 根据用户ID删除
	 * @param certId
	 */
    void deleteByCertId(Integer certId);

	/**
	 * 根据手机号查询
	 * @param username
	 * @return
	 */
	List<CertConfirm> selectByPhone(String username);
}