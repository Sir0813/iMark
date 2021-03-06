package com.dm.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
public class CertFicate {

    private Integer certId;

    /**
     * 存证入链时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date certDate;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date certPostDate;

    private String certOwner;

    @NotBlank(message = "存证名称不能为空")
    @Size(max = 15, message = "存证名称最多15个字符")
    private String certName;

    private String certDescribe;

    /**
     * 0未存证(草稿) 1存证中 2存证成功(入链成功) 3存证失败 4待他人确认 5已退回 6已撤回
     */
    @NotNull(message = "存证状态不能为空")
    private Integer certStatus;

    /**
     * 存证是否删除 0 删除 1 未删除 逻辑删除
     */
    private Integer certIsDelete;

    /**
     * 0已确认1未确认
     */
    private Integer certIsconf;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date certDelDate;

    /**
     * 存证类型 1文件存证 2 拍照存证 3 相册存证 4 录像存证 5 录音存证 6 录屏存证 7 模板存证
     */
    @NotNull(message = "存证类型不能为空")
    private Integer certType;

    private String certHash;

    private String certChainno;

    private String certFilesid;

    /**
     * 是否将文件存入证云链 0 不保存 1 保存
     */
    private String certFileIsSave;


    /**
     * 文件签名
     */
    private String signature;

    /**
     * 确认人业务字段
     */
    @Valid
    private List<CertConfirm> certConfirmList;

    /**
     * 文件业务字段
     */
    private List<CertFiles> certFilesList;

    /**
     * 模板ID 业务字段
     */
    private Integer temId;

    private String certAddress;

    private String certCode;

    private String certBlockNumber;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCertBlockNumber() {
        return certBlockNumber;
    }

    public void setCertBlockNumber(String certBlockNumber) {
        this.certBlockNumber = certBlockNumber;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getCertAddress() {
        return certAddress;
    }

    public void setCertAddress(String certAddress) {
        this.certAddress = certAddress;
    }

    public Integer getTemId() {
        return temId;
    }

    public void setTemId(Integer temId) {
        this.temId = temId;
    }

    public String getCertFileIsSave() {
        return certFileIsSave;
    }

    public void setCertFileIsSave(String certFileIsSave) {
        this.certFileIsSave = certFileIsSave;
    }

    public String getCertFilesid() {
        return certFilesid;
    }

    public void setCertFilesid(String certFilesid) {
        this.certFilesid = certFilesid;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public Date getCertDate() {
        return certDate;
    }

    public void setCertDate(Date certDate) {
        this.certDate = certDate;
    }

    public String getCertOwner() {
        return certOwner;
    }

    public void setCertOwner(String certOwner) {
        this.certOwner = certOwner;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName == null ? null : certName.trim();
    }

    public String getCertDescribe() {
        return certDescribe;
    }

    public void setCertDescribe(String certDescribe) {
        this.certDescribe = certDescribe == null ? null : certDescribe.trim();
    }

    public Integer getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(Integer certStatus) {
        this.certStatus = certStatus;
    }

    public Integer getCertIsDelete() {
        return certIsDelete;
    }

    public void setCertIsDelete(Integer certIsDelete) {
        this.certIsDelete = certIsDelete;
    }

    public Date getCertDelDate() {
        return certDelDate;
    }

    public void setCertDelDate(Date certDelDate) {
        this.certDelDate = certDelDate;
    }

    public Integer getCertType() {
        return certType;
    }

    public void setCertType(Integer certType) {
        this.certType = certType;
    }

    public String getCertHash() {
        return certHash;
    }

    public void setCertHash(String certHash) {
        this.certHash = certHash == null ? null : certHash.trim();
    }

    public String getCertChainno() {
        return certChainno;
    }

    public void setCertChainno(String certChainno) {
        this.certChainno = certChainno == null ? null : certChainno.trim();
    }

    public Integer getCertIsconf() {
        return certIsconf;
    }

    public void setCertIsconf(Integer certIsconf) {
        this.certIsconf = certIsconf;
    }

    public List<CertConfirm> getCertConfirmList() {
        return certConfirmList;
    }

    public void setCertConfirmList(List<CertConfirm> certConfirmList) {
        this.certConfirmList = certConfirmList;
    }

    public Date getCertPostDate() {
        return certPostDate;
    }

    public void setCertPostDate(Date certPostDate) {
        this.certPostDate = certPostDate;
    }

    public List<CertFiles> getCertFilesList() {
        return certFilesList;
    }

    public void setCertFilesList(List<CertFiles> certFilesList) {
        this.certFilesList = certFilesList;
    }

}