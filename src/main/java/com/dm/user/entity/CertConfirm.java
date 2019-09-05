package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CertConfirm {
    private Integer confirmId;

    private Integer certId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date confirmDate;

    private String confirmPerson;

    /*0已确认1未确认*/
    private Integer confirmState;

    private Integer result;

    private String reasonCode;

    private String confirmPhone;

    private String returnReason;

    public Integer getConfirmId() {
        return confirmId;
    }

    public void setConfirmId(Integer confirmId) {
        this.confirmId = confirmId;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmPerson() {
        return confirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        this.confirmPerson = confirmPerson == null ? null : confirmPerson.trim();
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Integer confirmState) {
        this.confirmState = confirmState;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode == null ? null : reasonCode.trim();
    }

    public String getConfirmPhone() {
        return confirmPhone;
    }

    public void setConfirmPhone(String confirmPhone) {
        this.confirmPhone = confirmPhone == null ? null : confirmPhone.trim();
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}