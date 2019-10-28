package com.dm.user.msg;

/**
 * @author cui
 * @date 2019/10/22
 * @hour 13
 */
public enum CertStateEnum {

    NO_CERT(0, "草稿"),
    TO_CERT(1, "存证中"),
    CERT_SUCCESS(2, "存证成功"),
    CERT_FAIL(3, "存证失败"),
    OTHERS_CONFIRM(4, "待他人确认"),
    IS_RETURN(5, "失效 已退回"),
    IS_REVOKE(6, "失效 已撤回");

    private int code;
    private String desc;

    private CertStateEnum(int code, String desc) {
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
