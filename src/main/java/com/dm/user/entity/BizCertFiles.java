package com.dm.user.entity;

import java.io.Serializable;

/**
 * (BizCertFiles)实体类
 *
 * @author makejava
 * @since 2020-05-28 10:29:16
 */
public class BizCertFiles implements Serializable {
    private static final long serialVersionUID = -60703115660010901L;
    /**
    * 主键ID
    */
    private Integer fileId;
    /**
    * 文件类型(存储文件后缀)
    */
    private String fileType;
    /**
    * 文件名称(标题)
    */
    private String fileName;
    /**
    * 文件大小
    */
    private Object fileSize;
    /**
    * 文件存储路径
    */
    private String filePath;
    /**
    * 文件顺序
    */
    private String fileSeq;
    /**
    * 存证id
    */
    private Integer certId;
    /**
    * 访问路径
    */
    private String fileUrl;
    /**
    * 文件hash
    */
    private String fileHash;
    /**
    * 缩略图路径
    */
    private String thumbUrl;
    /**
    * 文件描述信息
    */
    private String detail;


    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getFileSize() {
        return fileSize;
    }

    public void setFileSize(Object fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSeq() {
        return fileSeq;
    }

    public void setFileSeq(String fileSeq) {
        this.fileSeq = fileSeq;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}