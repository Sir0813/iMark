package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CertVerify {
    private Integer verifyId;

    private Integer certId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date verifyDate;

    private Integer verifyState;

    private Integer verifyMoney;

    public Integer getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Integer verifyId) {
        this.verifyId = verifyId;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Integer getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(Integer verifyState) {
        this.verifyState = verifyState;
    }

    public Integer getVerifyMoney() {
        return verifyMoney;
    }

    public void setVerifyMoney(Integer verifyMoney) {
        this.verifyMoney = verifyMoney;
    }
}