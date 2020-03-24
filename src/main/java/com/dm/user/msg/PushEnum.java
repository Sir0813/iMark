package com.dm.user.msg;

public enum PushEnum {

    CERT_MSG("1", "存证消息"),
    PAY_MSG("2", "支付消息"),
    REVIEW_MSG("3", "审核消息");

    private String code;
    private String desc;

    PushEnum(String code, String desc) {
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
