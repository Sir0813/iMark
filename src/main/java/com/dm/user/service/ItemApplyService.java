package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.ItemApply;
import com.dm.user.entity.ItemApplyFiles;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface ItemApplyService {

    /**
     * 申请公正列表
     *
     * @param pageNum
     * @return
     */
    PageInfo<ItemApply> list(int pageNum) throws Exception;

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
     * 待接单列表
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    Result pendList(Page<ItemApply> page, int itemId) throws Exception;

    /**
     * 接单
     *
     * @param itemApply
     * @return
     * @throws Exception
     */
    Result takeOrder(ItemApply itemApply) throws Exception;

    /**
     * 已接单待审核
     *
     * @param page
     * @param itemId
     * @return
     * @throws Exception
     */
    Result pendReview(Page<ItemApply> page, int itemId, int type) throws Exception;

    /**
     * 材料退回
     *
     * @param map
     * @return
     * @throws Exception
     */
    Result rejectReason(Map<String, Object> map) throws Exception;

    /**
     * 材料通过
     *
     * @param itemApplyFiles
     * @return
     * @throws Exception
     */
    Result pass(ItemApplyFiles itemApplyFiles) throws Exception;

    /**
     * 添加修改文件批注
     *
     * @param map
     * @return
     * @throws Exception
     */
    Result notes(Map<String, Object> map) throws Exception;

    /**
     * 审批待处理列表
     *
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<ItemApply> waitList(Page<ItemApply> page, int itemId) throws Exception;

    /**
     * 已处理列表
     *
     * @param page
     * @param itemId
     * @return
     * @throws Exception
     */
    PageInfo<ItemApply> dealList(Page<ItemApply> page, int itemId) throws Exception;

    /**
     * 审批通过
     *
     * @param map
     */
    void approved(Map<String, Object> map) throws Exception;

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
     * 提交自定义价格
     *
     * @param map
     * @return
     */
    Result submitCustomPrice(Map<String, Object> map) throws Exception;

    /**
     * 支付尾款
     *
     * @param itemApply
     * @return
     * @throws Exception
     */
    Result payBalance(ItemApply itemApply) throws Exception;
}
