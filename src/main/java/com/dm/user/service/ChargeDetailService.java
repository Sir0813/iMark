package com.dm.user.service;

import com.dm.user.entity.ApplyFee;
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

    /**
     * 查询预付款
     *
     * @return
     */
    List<ApplyFee> selectByApplyId(int applyId);

    /**
     * 副本费
     *
     * @param applyid
     * @return
     */
    ApplyFee selectByApplyIdAndFileNum(Integer applyid);

    /**
     * 首款支付修改状态
     *
     * @param chargeMap
     */
    void updateByIdAndApplyId(Map<String, Object> chargeMap);

    /**
     * 查询
     *
     * @param applyid
     * @return
     */
    ApplyFee selectByApplyIdAndStatus(int applyid);

    /**
     * 修改支付状态
     *
     * @param id
     */
    void updatePayStatus(Integer id);
}
