package com.dm.user.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class UserCard {
    private Integer cardid;

    private String realName;

    private String cardType;

    private String cardNumber;

    private String frontPath;

    private String backPath;

    private Integer userid;

    private String realState;

    private Date postTime;

    private Date realTime;

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber == null ? null : cardNumber.trim();
    }

    public String getFrontPath() {
        return frontPath;
    }

    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath == null ? null : frontPath.trim();
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath == null ? null : backPath.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRealState() {
        return realState;
    }

    public void setRealState(String realState) {
        this.realState = realState == null ? null : realState.trim();
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public Date getRealTime() {
        return realTime;
    }

    public void setRealTime(Date realTime) {
        this.realTime = realTime;
    }
}