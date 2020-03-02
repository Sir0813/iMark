package com.dm.user.mapper;

import com.dm.user.entity.ItemApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2020-01-02
 */
@Mapper
public interface ItemApplyMapper {

    /**
     * 删除
     *
     * @param applyid
     * @return
     */
    int deleteByPrimaryKey(Integer applyid);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(ItemApply record);

    /**
     * 根据主键查询
     *
     * @param applyid
     * @return
     */
    ItemApply selectByPrimaryKey(Integer applyid);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ItemApply record);

    /**
     * 申请公正列表
     *
     * @return
     */
    List<ItemApply> list(int userid);

    /**
     * 材料待接单列表
     *
     * @param map
     * @return
     */
    List<ItemApply> pendList(Map<String, Object> map);

    /**
     * 已接单待审核
     *
     * @param map
     * @return
     */
    List<ItemApply> pendReview(Map<String, Object> map);

    /**
     * 查询当前用户待审理数据
     *
     * @param map
     * @return
     */
    List<ItemApply> selectWaitList(Map<String, Object> map);

    /**
     * 已处理列表
     *
     * @param map
     * @return
     */
    List<ItemApply> dealList(Map<String, Object> map);

    /**
     * 修改公正状态
     *
     * @param map
     */
    void updateState(Map<String, Object> map);
}