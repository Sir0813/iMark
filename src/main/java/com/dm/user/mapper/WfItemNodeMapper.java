package com.dm.user.mapper;

import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.WfItemNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
     * 流程模板
     *
     * @param userId
     * @return
     */
    List<ProcessConfig> selectByUserId(String userId);
}