package com.dm.user.entity;

public class WfItemNode {
    private Integer id;

    private String itemid;

    private Integer order;

    private String nodename;

    private String describe;

    private String auditUserid;

    private String createdDate;

    private String createdBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid == null ? null : itemid.trim();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename == null ? null : nodename.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(String auditUserid) {
        this.auditUserid = auditUserid == null ? null : auditUserid.trim();
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate == null ? null : createdDate.trim();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }
}