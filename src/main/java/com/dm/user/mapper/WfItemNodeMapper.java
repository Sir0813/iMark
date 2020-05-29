package com.dm.user.mapper;

import com.dm.user.entity.ProcessConfig;
import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfItemNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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
     * 流程模板
     *
     * @param userId
     * @return
     */
    List<ProcessConfig> selectByUserId(String userId);

    /**
     * 刪除之前模板
     *
     * @param map
     */
    void deleteByUserId(Map<String, Object> map);

    /**
     * 新增
     *
     * @param wfItemNode
     */
    void insertData(WfItemNode wfItemNode);

    /**
     * 未审批节点
     *
     * @param applyid
     * @return
     */
    List<ProgressView> isNotProgress(Integer applyid);
}