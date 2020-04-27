package com.dm.user.service;

import com.dm.user.entity.ItemApplyLog;

import java.util.Date;
import java.util.List;

public interface ItemApplyLogService {

    /**
     * 记录日志
     *
     * @param createdBy
     * @param applyId
     * @param date
     * @param status
     */
    void insertLog(String createdBy, Integer applyId, Date date, Integer status) throws Exception;

    /**
     * 历史记录
     *
     * @param applyid
     * @return
     * @throws Exception
     */
    List<ItemApplyLog> historyNode(int applyid) throws Exception;

    /**
     * 根据订单号和订单状态查询
     *
     * @param applyid
     * @param code
     * @return
     * @throws Exception
     */
    ItemApplyLog selectByApplyIdAndStatus(Integer applyid, int code) throws Exception;

    /**
     * 主键修改
     *
     * @param itemApplyLog
     * @throws Exception
     */
    void updateById(ItemApplyLog itemApplyLog) throws Exception;
}
