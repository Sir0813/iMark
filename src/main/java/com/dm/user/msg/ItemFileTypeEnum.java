package com.dm.user.msg;

public enum ItemFileTypeEnum {

    USER_FILE(1, "用户公正上传的文件"),
    OPINION_FILE(2, "公正意见书"),
    FAIR_VIDO(3, "公正视频"),
    SEAL_OPINION_FILE(4, "盖章公正意见书"),
    FILE_EXISTENCE(1, "文件存在"),
    FILE_NOT_EXISTENCE(0, "文件逻辑删除");

    private int code;
    private String desc;

    ItemFileTypeEnum(int code, String desc) {
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
