package com.dm.user.entity;

import java.util.List;

public class ProcessConfigView {

    private Integer applyid;

    private List<ProcessConfig> processConfigList;

    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public List<ProcessConfig> getProcessConfigList() {
        return processConfigList;
    }

    public void setProcessConfigList(List<ProcessConfig> processConfigList) {
        this.processConfigList = processConfigList;
    }
}
