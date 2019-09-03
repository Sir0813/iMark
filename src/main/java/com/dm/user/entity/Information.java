package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Information {
    private Integer infoId;

    private String infoCode;

    private String infoMsg;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date infoSenddate;

    private String infoUser;

    /*0发送成功未进行认证（有效）1发送成功已经认证（无效）*/
    private String infoState;

    private String infoPhone;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date infoExpireddate;

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode == null ? null : infoCode.trim();
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg == null ? null : infoMsg.trim();
    }

    public Date getInfoSenddate() {
        return infoSenddate;
    }

    public void setInfoSenddate(Date infoSenddate) {
        this.infoSenddate = infoSenddate;
    }

    public String getInfoUser() {
        return infoUser;
    }

    public void setInfoUser(String infoUser) {
        this.infoUser = infoUser == null ? null : infoUser.trim();
    }

    public String getInfoPhone() {
        return infoPhone;
    }

    public void setInfoPhone(String infoPhone) {
        this.infoPhone = infoPhone == null ? null : infoPhone.trim();
    }

    public Date getInfoExpireddate() {
        return infoExpireddate;
    }

    public void setInfoExpireddate(Date infoExpireddate) {
        this.infoExpireddate = infoExpireddate;
    }

	public String getInfoState() {
		return infoState;
	}

	public void setInfoState(String infoState) {
		this.infoState = infoState;
	}
}