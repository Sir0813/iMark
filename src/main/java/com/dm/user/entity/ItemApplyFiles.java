package com.dm.user.entity;

import java.util.Date;

public class ItemApplyFiles {
    private Integer id;

    private Integer applyid;

    private Integer fileid;

    private Integer requeredid;

    private String describe;

    private Date createdDate;

    private int fileTypes;
    private Integer supplementId;
    private Integer fileLogId;

    private String fileString;

    /**
     * 1 存在 0 删除
     */
    private int isDel;

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public Integer getFileid() {
        return fileid;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }

    public Integer getRequeredid() {
        return requeredid;
    }

    public void setRequeredid(Integer requeredid) {
        this.requeredid = requeredid;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(int fileTypes) {
        this.fileTypes = fileTypes;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public Integer getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(Integer supplementId) {
        this.supplementId = supplementId;
    }

    public Integer getFileLogId() {
        return fileLogId;
    }

    public void setFileLogId(Integer fileLogId) {
        this.fileLogId = fileLogId;
    }
}
