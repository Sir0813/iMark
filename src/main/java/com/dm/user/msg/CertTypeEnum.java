package com.dm.user.msg;

/**
 * @author cui
 * @date 2019/10/22
 * @hour 14
 */
public enum CertTypeEnum {

    FILECERT(1, "文件存证"),
    TAKEPHOTO(2, "拍照存证"),
    PHOTO(3, "相册存证"),
    VIDEO(4, "录像存证"),
    RECORDING(5, "录音存证"),
    SCREENCAP(6, "录屏存证"),
    TEMPLATE(7, "模板存证");

    private int code;
    private String desc;
    private CertTypeEnum(int code, String desc) {
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
