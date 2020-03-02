package com.dm.user.msg;

public enum ItemApplyEnum {
    DRAFT(1, "草稿"),
    REVIEW(2, "材料审核中"),
    REVIEW_YES(3, "材料审核通过"),
    REVIEW_NO(4, "公正失败"),
    REVIEW_SUCCESS(5, "公正完成");

    private int code;
    private String desc;

    ItemApplyEnum(int code, String desc) {
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
