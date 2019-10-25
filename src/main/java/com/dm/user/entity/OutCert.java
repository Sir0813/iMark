package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
public class OutCert {

    private Integer outCertId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date outCertTime;

    @NotBlank(message = "出证名称不能为空")
    @Size(max = 15, message = "出证名称最多15个字符")
    private String outCertName;

    @NotBlank(message = "出证说明不能为空")
    @Size(max = 50, message = "出证说明最多50个字符")
    private String outCertDesc;

    private Integer userId;

    private Integer fileId;

    private String certId;

    /** 发起人 业务字段 */
    private String promoter;

    /** 出证文件路径 */
    private String outCertExplain;

    private List<CertFiles> list;

    /** 发送人业务字段 */
    private List<Contact> contactList;

    public List<CertFiles> getList() {
        return list;
    }

    public void setList(List<CertFiles> list) {
        this.list = list;
    }

    public String getOutCertExplain() {
        return outCertExplain;
    }

    public void setOutCertExplain(String outCertExplain) {
        this.outCertExplain = outCertExplain;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

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