package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ItemRequered {
    private Integer requeredid;

    private Integer itemid;

    private String name;

    private String descibe;

    private String logoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    private Integer createdBy;

    /**
     * 公正文件业务字段
     */
    private List<CertFiles> certFilesList;

    public Integer getRequeredid() {
        return requeredid;
    }

    public void setRequeredid(Integer requeredid) {
        this.requeredid = requeredid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescibe() {
        return descibe;
    }

    public void setDescibe(String descibe) {
        this.descibe = descibe == null ? null : descibe.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public List<CertFiles> getCertFilesList() {
        return certFilesList;
    }

    public void setCertFilesList(List<CertFiles> certFilesList) {
        this.certFilesList = certFilesList;
    }
}