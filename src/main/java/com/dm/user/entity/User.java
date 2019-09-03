package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class User {
    private Integer userid;

    private String usercode;

    private String username;

    private String mobile;

    private String email;

    private String password;

    private String sex;

    private Integer isEnabled;

    private Integer isExpired;

    private Integer isCredentialsExpired;

    private Integer isLocked;

    private String salt;

    private Integer badLoginNum;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastPwdDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastActive;

    private String createdDate;

    private Integer source;

    private String describe;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date validEndDate;
    
    private String headPhoto;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public Integer getIsCredentialsExpired() {
        return isCredentialsExpired;
    }

    public void setIsCredentialsExpired(Integer isCredentialsExpired) {
        this.isCredentialsExpired = isCredentialsExpired;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getBadLoginNum() {
        return badLoginNum;
    }

    public void setBadLoginNum(Integer badLoginNum) {
        this.badLoginNum = badLoginNum;
    }

    public Date getLastPwdDate() {
        return lastPwdDate;
    }

    public void setLastPwdDate(Date lastPwdDate) {
        this.lastPwdDate = lastPwdDate;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}
}