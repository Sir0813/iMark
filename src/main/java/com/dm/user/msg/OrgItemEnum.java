package com.dm.user.msg;

public enum OrgItemEnum {

    FIXED_PRICE(1, "固定价格"),
    SUBJECT_PRICE(2, "按标的计价"),
    CUSTOM_PRICE(3, "公正人员自定义价格"),
    DRAFT(1, "草稿"),
    SHELVES(2, "正常上架"),
    TAKEOFF(3, "下架");


    private int code;
    private String desc;

    OrgItemEnum(int code, String desc) {
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
