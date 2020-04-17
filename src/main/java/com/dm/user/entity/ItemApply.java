package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ItemApply {
    private Integer applyid;

    private String applyNo;

    private Integer itemid;

    private Integer userid;

    private Integer status;

    private String describe;

    private Integer certid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitDate;

    private Double itemValue;

    private Double price;

    private String wfInstanceId;

    private Date handleCreatedDate;

    private Integer handleUserid;

    private Integer payStatus;

    private Double payEndPrice;

    private String itemCode;

    /**
     * 公正文件业务字段
     */
    private List<ApplyFile> applyFileList;

    /**
     * 公正名称业务字段
     */
    private String itemName;

    /**
     * 用户真实姓名业务字段
     */
    private String realName;

    /**
     * 公正logo业务字段
     */
    private String logoUrl;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo == null ? null : applyNo.trim();
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public Integer getCertid() {
        return certid;
    }

    public void setCertid(Integer certid) {
        this.certid = certid;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public List<ApplyFile> getApplyFileList() {
        return applyFileList;
    }

    public void setApplyFileList(List<ApplyFile> applyFileList) {
        this.applyFileList = applyFileList;
    }

    public String getWfInstanceId() {
        return wfInstanceId;
    }

    public void setWfInstanceId(String wfInstanceId) {
        this.wfInstanceId = wfInstanceId;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getHandleCreatedDate() {
        return handleCreatedDate;
    }

    public void setHandleCreatedDate(Date handleCreatedDate) {
        this.handleCreatedDate = handleCreatedDate;
    }

    public Integer getHandleUserid() {
        return handleUserid;
    }

    public void setHandleUserid(Integer handleUserid) {
        this.handleUserid = handleUserid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Double getPayEndPrice() {
        return payEndPrice;
    }

    public void setPayEndPrice(Double payEndPrice) {
        this.payEndPrice = payEndPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}