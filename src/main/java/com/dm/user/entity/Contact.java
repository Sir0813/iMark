package com.dm.user.entity;

/**
 * @author cui
 * @date 2019-09-26
 */
public class Contact {
    private Integer contactId;

    private Integer userId;

    private Integer outCertId;

    private String contactPerson;

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