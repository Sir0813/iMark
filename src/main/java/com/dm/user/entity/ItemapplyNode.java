package com.dm.user.entity;

public class ItemapplyNode {

    private Integer status;

    private String nodeName;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public ItemapplyNode(Integer status, String nodeName) {
        this.status = status;
        this.nodeName = nodeName;
    }
}
