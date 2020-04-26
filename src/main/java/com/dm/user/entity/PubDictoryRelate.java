package com.dm.user.entity;

public class PubDictoryRelate {
    private Integer id;

    private Integer dictoryId;

    private String childId;

    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDictoryId() {
        return dictoryId;
    }

    public void setDictoryId(Integer dictoryId) {
        this.dictoryId = dictoryId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId == null ? null : childId.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}