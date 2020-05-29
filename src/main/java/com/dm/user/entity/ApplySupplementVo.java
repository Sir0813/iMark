package com.dm.user.entity;

import java.util.List;
import java.util.Map;

public class ApplySupplementVo {
    private List<Map<String,Object>>  eidtList;
    private List<Map<String,Object>>  supplementList;
    private Integer applyId;

    public List<Map<String, Object>> getEidtList() {
        return eidtList;
    }

    public void setEidtList(List<Map<String, Object>> eidtList) {
        this.eidtList = eidtList;
    }

    public List<Map<String, Object>> getSupplementList() {
        return supplementList;
    }

    public void setSupplementList(List<Map<String, Object>> supplementList) {
        this.supplementList = supplementList;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }
}
