package com.dm.user.mapper;

import com.dm.user.entity.*;
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
     * 我的公正(用户页面)
     *
     * @param map
     * @return
     */
    List<ItemApply> list(Map<String, Object> map);

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

    /**
     * 公正审核历史记录
     *
     * @param applyid
     * @return
     */
    List<ApplyHistory> history(int applyid);

    /**
     * 新订单数量
     *
     * @param status
     * @return
     */
    Integer selectOrderCount(int status);

    /**
     * 处理中数量
     *
     * @param dataMap
     * @return
     */
    Integer inProcessing(Map<String, Object> dataMap);

    /**
     * 已处理数量
     *
     * @param dataMap
     * @return
     */
    Integer isProcessing(Map<String, Object> dataMap);

    /**
     * 我的申请前三条
     *
     * @param dataMap
     * @return
     */
    List<ItemApply> selectMyApply(Map<String, Object> dataMap);

    /**
     * 最新申请前三条
     *
     * @param status
     * @return
     */
    List<ItemApply> selectNewApply(int status);

    /**
     * 待复核数量
     *
     * @param dataMap
     * @return
     */
    Integer selectWaitApplyCount(Map<String, Object> dataMap);

    /**
     * 我的申请总数量
     *
     * @param dataMap
     * @return
     */
    Integer selectMyApplyCount(Map<String, Object> dataMap);

    /**
     * 申请详情信息
     *
     * @param dataMap
     * @return
     */
    ApplyUserInfo selectDetailInfo(Map<String, Object> dataMap);

    /**
     * 受理进度信息查询
     *
     * @param applyid
     * @return
     */
    ApplyUserInfo selectApplyInfo(int applyid);

    /**
     * 已初审通过
     *
     * @param status
     * @return
     */
    Integer isVerifi(int status);

    /**
     * 公正复核数量
     *
     * @param map
     * @return
     */
    Integer inReview(Map<String, Object> map);

    /**
     * 正在受理
     *
     * @param map
     * @return
     */
    List<ItemApply> inProcessingList(Map<String, Object> map);

    /**
     * 我的任务列表
     *
     * @param map
     * @return
     */
    List<ItemApply> selectMyTaskList(Map<String, Object> map);

    /**
     * 预审驳回
     *
     * @param map
     */
    void updateRejectReason(Map<String, Object> map);

    /**
     * 预审驳回退费详情页
     *
     * @param applyid
     * @return
     */
    OrderInfo orderInfo(int applyid);

    /**
     * 收费明细
     *
     * @param applyid
     * @return
     */
    List<ChargeDetail> feeInfo(int applyid);
}