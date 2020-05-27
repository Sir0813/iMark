package com.dm.user.entity;

import java.util.List;

public class ApplyFeeView {

    private Integer applyid;

    private List<ApplyFee> applyFees;

    private double payEndPrice;

    public double getPayEndPrice() {
        return payEndPrice;
    }

    public void setPayEndPrice(double payEndPrice) {
        this.payEndPrice = payEndPrice;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public List<ApplyFee> getApplyFees() {
        return applyFees;
    }

    public void setApplyFees(List<ApplyFee> applyFees) {
        this.applyFees = applyFees;
    }
}
