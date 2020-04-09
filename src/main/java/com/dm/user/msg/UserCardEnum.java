package com.dm.user.msg;

/**
 * @author cui
 * @date 2019/10/23
 * @hour 15
 */
public enum UserCardEnum {

    NOT_REAL("1", "提交认证"),
    REAL_SUCCESS("2", "认证成功"),
    IS_REAL_SUCCESS("3", "已完成实名认证再次认证只能本人");

    private String code;
    private String desc;

    UserCardEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
