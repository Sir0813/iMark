package com.dm.user.mapper;

import com.dm.user.entity.TemFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface TemFileMapper {

    /**
     * 删除
     *
     * @param temId
     * @return
     */
    int deleteByPrimaryKey(Integer temId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(TemFile record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(TemFile record);

    /**
     * 根据ID查询
     *
     * @param temId
     * @return
     */
    TemFile selectByPrimaryKey(Integer temId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TemFile record);

    /**
     * 根据存证ID查询
     *
     * @param certId
     * @return
     */
    TemFile selectByCertId(String certId);
}