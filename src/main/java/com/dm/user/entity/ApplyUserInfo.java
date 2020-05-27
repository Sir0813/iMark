package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ApplyUserInfo {

    /**
     * 申请人姓名
     */
    private String realName;

    /**
     * 申请人手机号
     */
    private String userPhone;

    /**
     * 申请人身份证号
     */
    private String cardNumber;

    /**
     * 0 自取 1 邮寄
     */
    private int isSend;

    /**
     * isSend为0显示公证处地址
     */
    private String orgAddress;

    /**
     * isSend为1显示邮寄地址
     */
    private UserAddress userAddress;

    /**
     * 公正名称
     */
    private String itemName;

    /**
     * 订单编号
     */
    private String applyNo;

    /**
     * 公正申办日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitDate;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 公正处名称
     */
    private String orgName;

    /**
     * 公正项目描述
     */
    private String itemDesc;

    /**
     * 申请公正描述
     */
    private String describe;

    /**
     * 公正标的价格
     */
    private Double itemValue;

    /**
     * 公正状态
     */
    private Integer status;

    /**
     * 公正计价方式
     */
    private Integer valuation;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 固定金额或最低收费金额
     */
    private Double price;

    /**
     * 公正申请ID
     */
    private Integer applyid;

    /**
     * 补充修改材料
     */
    private Integer addFileStatus;

    public Integer getAddFileStatus() {
        return addFileStatus;
    }

    public void setAddFileStatus(Integer addFileStatus) {
        this.addFileStatus = addFileStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getValuation() {
        return valuation;
    }

    public void setValuation(Integer valuation) {
        this.valuation = valuation;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
