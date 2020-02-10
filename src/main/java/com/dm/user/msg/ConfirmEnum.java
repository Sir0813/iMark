package com.dm.user.msg;

/**
 * @author cui
 * @date 2019/10/22
 * @hour 14
 */
public enum ConfirmEnum {

    IS_CONFIRM(0, "已确认"),
    NO_CONFIRM(1, "未确认"),
    RETURN_CONFIRM(2, "已退回"),
    REVOKE_CONFIRM(3, "已撤回"),
    ORIGINATOR(4, "发起人");

    private int code;
    private String desc;

    ConfirmEnum(int code, String desc) {
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
