package com.dm.user.mapper;

import com.dm.user.entity.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface InformationMapper {

    /**
     * 删除
     *
     * @param infoId
     * @return
     */
    int deleteByPrimaryKey(Integer infoId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(Information record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(Information record);

    /**
     * 根据ID查询
     *
     * @param infoId
     * @return
     */
    Information selectByPrimaryKey(Integer infoId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Information record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Information record);

    /**
     * 根据手机号查询
     *
     * @param map
     * @return
     */
    Information selectByPhone(Map<String, Object> map);
}