package com.dm.user.msg;

public enum InstanceEnum {

    PENDING_REVIEW(1, "待审核"),
    UNDER_REVIEW(2, "审核中"),
    REVIEW_PASS(3, "审核通过"),
    REVIEW_NOT_PASS(4, "审核未通过");

    private int code;
    private String desc;

    InstanceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
