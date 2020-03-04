package com.dm.user.mapper;

import com.dm.user.entity.WfItemNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

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
     * 根据项目ID查询第一个节点
     *
     * @param itemid
     * @return
     */
    WfItemNode selectByItemId(String itemid);

    /**
     * 根据项目ID查询最后一个节点
     *
     * @param toString
     * @return
     */
    WfItemNode selectByItemIdDesc(String toString);

    /**
     * 节点和itemid查询
     *
     * @param map
     * @return
     */
    WfItemNode selectByOrder(Map<String, Object> map);
}