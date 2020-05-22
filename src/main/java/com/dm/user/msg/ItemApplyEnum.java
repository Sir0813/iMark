package com.dm.user.msg;

public enum ItemApplyEnum {
    WAIT_ORDER(0, "材料初审"),
    FILE_DRAFT(1, "草稿"),
    WAIT_PAY(2, "待支付"),
    FILE_RECEPTION(3, "材料接收"),
    FILE_CHECK(4, "材料复核"),
    FILE_REVIEW(5, "审批"),
    FILE_MAKE(6, "制证"),
    FILE_SEND(7, "发证"),
    APPLY_SUCCESS(8, "完成公正"),
    APPLY_FAIL(9, "公正失败"),
    REJECT_REASON(10, "材料初审未通过"),
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
