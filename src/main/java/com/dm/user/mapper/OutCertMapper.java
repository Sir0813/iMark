package com.dm.user.mapper;

import com.dm.user.entity.OutCert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface OutCertMapper {

    /**
     * 删除
     *
     * @param outCertId
     * @return
     */
    int deleteByPrimaryKey(Integer outCertId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(OutCert record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(OutCert record);

    /**
     * 根据ID查询
     *
     * @param outCertId
     * @return
     */
    OutCert selectByPrimaryKey(Integer outCertId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OutCert record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(OutCert record);

    /**
     * 根据用户ID查询
     *
     * @param userId
     * @return
     */
    List<OutCert> list(String userId);
}