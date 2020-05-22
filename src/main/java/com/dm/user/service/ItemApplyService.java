package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.ItemApply;
import com.dm.user.entity.Reject;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface ItemApplyService {

    /**
     * 用户端公正申请列表
     *
     * @param pageNum 分页起始页
     * @param type    分类 0 全部 1 草稿 2 已递交 3 已完成 4 我的预约
     * @param word    模糊搜索关键字
     * @return
     * @throws Exception
     */
    PageInfo<ItemApply> list(int pageNum, int type, String word) throws Exception;

    /**
     * 用户已经提交的公正申请详情及办理进度
     *
     * @param applyid
     * @return
     */
    Map<String, Object> detail(int applyid) throws Exception;

    /**
     * 用户办理公正申请
     *
     * @param itemApply
     * @throws Exception
     */
    Result insert(ItemApply itemApply) throws Exception;

    /**
     * 删除草稿
     *
     * @param applyid
     * @return
     */
    Result delete(int applyid) throws Exception;

    /**
     * 接单
     *
     * @param itemApply
     * @return
     * @throws Exception
     */
    Result takeOrder(ItemApply itemApply) throws Exception;

    /**
     * 添加修改文件批注
     *
     * @param map
     * @return
     * @throws Exception
     */
    Result notes(Map<String, Object> map) throws Exception;

    /**
     * 审批通过
     *
     * @param map
     */
//    void approved(Map<String, Object> map) throws Exception;

    /**
     * 更改已提交订单为草稿
     *
     * @param itemApply
     * @return
     */
    Result updateToDraft(ItemApply itemApply) throws Exception;

    /**
     * 支付金额提交公正
     *
     * @param itemApply
     * @return
     * @throws Exception
     */
    Result submitApply(ItemApply itemApply) throws Exception;

    /**
     * 支付尾款
     *
     * @param itemApply
     * @return
     * @throws Exception
     */
//    Result payBalance(ItemApply itemApply) throws Exception;

    /**
     * 掌上公正首页
     *
     * @throws Exception
     */
    Result index() throws Exception;

    /**
     * 受理进度
     *
     * @param applyid
     * @return
     */
    Result acceptProgress(int applyid) throws Exception;

    /**
     * 我的任务首页
     *
     * @return
     */
    Result myTaskIndex() throws Exception;

    /**
     * 我的任务不同列表
     *
     * @param pageNum
     * @param type
     * @param itemId
     * @param word
     * @return
     */
    Result myTaskList(int pageNum, int type, Integer itemId, String word) throws Exception;

    /**
     * 公正管理端详情
     *
     * @param applyid
     * @return
     */
    Result mytaskDetail(int applyid) throws Exception;

    /**
     * 预审通过
     *
     * @param applyid
     * @return
     */
    Result mytaskPassed(int applyid) throws Exception;

    /**
     * 预审驳回
     *
     * @param reject
     * @return
     */
    Result mytaskRejectReason(Reject reject) throws Exception;

    /**
     * 预审驳回退费详情信息
     *
     * @param applyid
     * @return
     */
    Result rejectDetail(int applyid) throws Exception;

    /**
     * 批注记录
     *
     * @param id
     * @return
     * @throws Exception
     */
    Result notesList(int id) throws Exception;

    /**
     * 审批流程模板
     *
     * @return
     */
    Result processTemplate() throws Exception;

    /**
     * 公正复核待审批-已审批列表
     *
     * @param pageNum
     * @param word
     * @param type
     * @param itemId
     * @return
     */
    Result reviewList(Integer pageNum, String word, int type, int itemId) throws Exception;

    /**
     * 新增审批人-选择列表
     *
     * @return
     * @throws Exception
     */
    Result processList() throws Exception;
}
