package com.dm.user.msg;

public enum ItemApplyEnum {
    DRAFT(1, "草稿"),
    REVIEW(2, "材料审核中"),
    REVIEW_YES(3, "材料审核通过"),
    REVIEW_NO(4, "公正失败"),
    REVIEW_SUCCESS(5, "公正完成"),
    WAIT_PAY(6, "待支付"),
    SUBMISSION(7, "提交盖章意见书"),
    PAY_DEFAULT(0, "默认值"),
    PAY_PRE(1, "预付款"),
    PAY_BALANCE(2, "待结清尾款"),
    PAY_ALL(3, "结清费用");

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
