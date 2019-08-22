package com.dm.user.entity;

public class TemFile {
    private Integer temId;

    private Integer certId;

    private String temFileText;

    public Integer getTemId() {
        return temId;
    }

    public void setTemId(Integer temId) {
        this.temId = temId;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public String getTemFileText() {
        return temFileText;
    }

    public void setTemFileText(String temFileText) {
        this.temFileText = temFileText == null ? null : temFileText.trim();
    }
}