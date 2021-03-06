package com.dm.user.entity;

/**
 * 审批配置
 */
public class ProcessConfig {

    private Integer id;

    private Integer auditUserid;

    private String realName;

    private String userName;

    private int order;

    private String headPhoto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(Integer auditUserid) {
        this.auditUserid = auditUserid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
