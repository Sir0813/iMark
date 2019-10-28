package com.dm.user.mapper;

import com.dm.user.entity.App;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface AppMapper {
    /**
     * 根据ID删除
     *
     * @param appId 主键ID
     * @return
     */
    int deleteByPrimaryKey(Integer appId);

    /**
     * 新增
     *
     * @param record app实体
     * @return
     */
    int insert(App record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(App record);

    /**
     * 根据ID查询
     *
     * @param appId
     * @return
     */
    App selectByPrimaryKey(Integer appId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(App record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(App record);

    /**
     * 获取app最新版本
     *
     * @param app
     * @return
     */
    App getAppVersion(App app);
}