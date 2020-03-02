package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class OrgItems {
    private Integer itemid;

    private Integer orgid;

    private String itemCode;

    private String itemName;

    private String itemDesc;

    private String logoUrl;

    private Integer status;

    private Integer version;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    private String createdBy;

    private int valuation;

    private Double price;

    private Double lowestPrice;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getOrgid() {
        return orgid;
    }

    public void setOrgid(Integer orgid) {
        this.orgid = orgid;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public int getValuation() {
        return valuation;
    }

    public void setValuation(int valuation) {
        this.valuation = valuation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }
}