package com.dm.user.entity;

/**
 * @author cui
 * @date 2019-09-26
 */
public class CustomMessageDomain {

    /**
     * 设备标识 用户ID 别名
     */
    private String alias;
    /**
     * 通知内容标题
     */
    private String notificationTitle;
    /**
     * 消息内容标题
     */
    private String msgTitle;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 扩展字段（通常传跳转的链接）
     */
    private String extrasParam;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getExtrasParam() {
        return extrasParam;
    }

    public void setExtrasParam(String extrasParam) {
        this.extrasParam = extrasParam;
    }

    @Override
    public String toString() {
        return "CustomMessageDomain [alias=" + alias + ", notificationTitle=" + notificationTitle + ", msgTitle="
                + msgTitle + ", msgContent=" + msgContent + ", extrasParam=" + extrasParam + ", getAlias()="
                + getAlias() + ", getNotificationTitle()=" + getNotificationTitle() + ", getMsgTitle()=" + getMsgTitle()
                + ", getMsgContent()=" + getMsgContent() + ", getExtrasParam()=" + getExtrasParam() + "]";
    }

}
