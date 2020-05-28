package com.dm.user.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 审核人员上传附件表
(BizItemAttachment)实体类
 *
 * @author makejava
 * @since 2020-05-28 12:41:19
 */
public class BizItemAttachment implements Serializable {
    private static final long serialVersionUID = -28892526597636620L;
    
    private Integer id;
    /**
    * 文件id
    */
    private Integer fileId;
    /**
    * 公证id
    */
    private Integer applyId;
    
    private Date createTime;
    /**
    * 描述
    */
    private String applyDesc;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc;
    }

}