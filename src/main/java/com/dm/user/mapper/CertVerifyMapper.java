package com.dm.user.mapper;

import com.dm.user.entity.CertVerify;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface CertVerifyMapper {
    /**
     * 删除
     *
     * @param verifyId
     * @return
     */
    int deleteByPrimaryKey(Integer verifyId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(CertVerify record);

    /**
     * 根据ID查询
     *
     * @param verifyId
     * @return
     */
    CertVerify selectByPrimaryKey(Integer verifyId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CertVerify record);

}