package com.dm.user.mapper;

import com.dm.user.entity.WfItemNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WfItemNodeMapper {

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    WfItemNode selectByPrimaryKey(Integer id);

    /**
     * 根据项目ID查询流程
     *
     * @param itemid
     * @return
     */
    WfItemNode selectByItemId(String itemid);
}