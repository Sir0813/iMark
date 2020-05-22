package com.dm.user.service;

import com.dm.user.entity.ChargeDetail;

import java.util.List;
import java.util.Map;

public interface ChargeDetailService {

    /**
     * 根据申请ID删除数据
     *
     * @param applyid
     */
    void deleteByApplyId(Integer applyid);

    /**
     * 批量新增
     *
     * @param chargeDetails
     */
    void insertList(List<ChargeDetail> chargeDetails);

    /**
     * 单个新增
     *
     * @param preCharge
     */
    void insertData(ChargeDetail preCharge);

    /**
     * 查询公正订单预付款金额
     *
     * @param map
     */
    ChargeDetail selectByChargeIdAndApplyId(Map<String, Object> map);

    /**
     * 主键查询
     *
     * @param id
     */
    ChargeDetail selectById(Integer id);
}
