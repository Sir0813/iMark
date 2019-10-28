package com.dm.user.entity;

import java.util.Date;

/**
 * @author cui
 * @date 2019-09-26
 */
public class PwdHistory {

    private String password;

    private Date pwdDate;

    private Integer userid;

    public Date getPwdDate() {
        return pwdDate;
    }

    public void setPwdDate(Date pwdDate) {
        this.pwdDate = pwdDate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}