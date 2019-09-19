package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class OutCert {
    private Integer outCertId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date outCertTime;

    private String outCertName;

    private String outCertDesc;

    private Integer userId;

    private Integer fileId;

    private String certId;

    /*发送人业务字段*/
    private List<Contact> contactList;

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public Integer getOutCertId() {
        return outCertId;
    }

    public void setOutCertId(Integer outCertId) {
        this.outCertId = outCertId;
    }

    public Date getOutCertTime() {
        return outCertTime;
    }

    public void setOutCertTime(Date outCertTime) {
        this.outCertTime = outCertTime;
    }

    public String getOutCertName() {
        return outCertName;
    }

    public void setOutCertName(String outCertName) {
        this.outCertName = outCertName == null ? null : outCertName.trim();
    }

    public String getOutCertDesc() {
        return outCertDesc;
    }

    public void setOutCertDesc(String outCertDesc) {
        this.outCertDesc = outCertDesc == null ? null : outCertDesc.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId == null ? null : certId.trim();
    }
}