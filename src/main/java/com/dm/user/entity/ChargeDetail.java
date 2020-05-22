package com.dm.user.entity;

import java.util.Date;

public class ChargeDetail {
    private Integer id;

    private Integer applyId;

    private Integer chargeId;

    private Double price;

    private Integer createdId;

    private Integer handleId;

    /**
     * 0 未支付 1 已支付
     */
    private Integer isPay;

    /**
     * 0 收费  1 退费
     */
    private Integer payStatus;

    private Integer fileNum;

    private Date cratedDate;

    private Integer isDel;

    /**
     * 收费名称业务字段
     */
    private String feeName;

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public Integer getHandleId() {
        return handleId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    public Date getCratedDate() {
        return cratedDate;
    }

    public void setCratedDate(Date cratedDate) {
        this.cratedDate = cratedDate;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}