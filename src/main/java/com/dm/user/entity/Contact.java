package com.dm.user.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author cui
 * @date 2019-09-26
 */
public class Contact {
    private Integer contactId;

    private Integer userId;

    private Integer outCertId;

    @NotBlank(message = "联系人姓名不能为空")
    @Size(min = 1, max = 30, message = "联系人姓名过长")
    private String contactPerson;

    @NotBlank(message = "联系人手机号不能为空")
    @Pattern(regexp = "^0?(13|14|15|17|18|19)[0-9]{9}$", message = "请输入正确的联系人手机号")
    private String contactPhone;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOutCertId() {
        return outCertId;
    }

    public void setOutCertId(Integer outCertId) {
        this.outCertId = outCertId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson == null ? null : contactPerson.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }
}