package com.dm.user.entity;

import java.util.List;

public class Reject {

    private int applyid;

    private String rejectReason;

    /**
     * 0 预审退费 1 接单后退费
     */
    private int type;

    private List<ChargeDetail> feeInfo;

    public int getApplyid() {
        return applyid;
    }

    public void setApplyid(int applyid) {
        this.applyid = applyid;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public List<ChargeDetail> getFeeInfo() {
        return feeInfo;
    }

    public void setFeeInfo(List<ChargeDetail> feeInfo) {
        this.feeInfo = feeInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
