package com.dm.user.entity;

import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;

public class CertFicate {
    private Integer certId;

    /*存证入链时间*/
    private Date certDate;
    
    /*提交时间*/
    private Date certPostDate;

    private String certOwner;

    private String certName;

    private String certDescribe;

    /* 0未存证(草稿) 1存证中 2存证成功(入链成功) 3存证失败
     * 4待他人确认 5已退回 6已撤回
     * */
    private Integer certStatus;

    /*存证是否删除 0 删除 1 未删除 逻辑删除*/
    private Integer certIsDelete;
    
    /*0已确认1未确认*/
    private Integer certIsconf;

    private Date certDelDate;

    /*存证类型 0指纹存证1数据存证*/
    private Integer certType;

    private String certHash;

    private String certChainno;
    
    private String certFilesid;
    
    /*确认人业务字段*/
    private List<CertConfirm> certConfirmList;

    /*文件业务字段*/
    private List<CertFiles> certFilesList;
    
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

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
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

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
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

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
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