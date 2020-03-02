package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Org {
    private Integer itemid;

    private String orgcode;

    private String orgname;

    private String orgtype;

    private String phone;

    private String address;

    private String postCode;

    private String logoUrl;

    private String picUrl;

    private String homeUrl;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registeDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditDate;

    private String serveDesc;

    private String latitude;

    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode == null ? null : orgcode.trim();
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname == null ? null : orgname.trim();
    }

    public String getOrgtype() {
        return orgtype;
    }

    public void setOrgtype(String orgtype) {
        this.orgtype = orgtype == null ? null : orgtype.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode == null ? null : postCode.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl == null ? null : homeUrl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRegisteDate() {
        return registeDate;
    }

    public void setRegisteDate(Date registeDate) {
        this.registeDate = registeDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getServeDesc() {
        return serveDesc;
    }

    public void setServeDesc(String serveDesc) {
        this.serveDesc = serveDesc == null ? null : serveDesc.trim();
    }
}