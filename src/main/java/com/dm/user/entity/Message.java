package com.dm.user.entity;

public class Message {

    private Integer senderId;

    private Integer reciverId;

    private String senderName;

    private String senderFaceUrl;

    private String reciverName;

    private String reciverFaceUrl;

    private int type;//消息类型  0 文字 1 图片

    private String msg; //发送消息

    private String createTime;//发送时间 消息时间格式(yyyy-MM-dd HH:mm:ss)

    private int onlineCount; //在线用户数

    private String chatId;

    public Message(Integer senderId, Integer reciverId, String senderName, String senderFaceUrl,
                   String reciverName, String reciverFaceUrl, int type, String msg,
                   String createTime, int onlineCount, String chatId) {
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.senderName = senderName;
        this.senderFaceUrl = senderFaceUrl;
        this.reciverName = reciverName;
        this.reciverFaceUrl = reciverFaceUrl;
        this.type = type;
        this.msg = msg;
        this.createTime = createTime;
        this.onlineCount = onlineCount;
        this.chatId = chatId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReciverId() {
        return reciverId;
    }

    public void setReciverId(Integer reciverId) {
        this.reciverId = reciverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderFaceUrl() {
        return senderFaceUrl;
    }

    public void setSenderFaceUrl(String senderFaceUrl) {
        this.senderFaceUrl = senderFaceUrl;
    }

    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    public String getReciverFaceUrl() {
        return reciverFaceUrl;
    }

    public void setReciverFaceUrl(String reciverFaceUrl) {
        this.reciverFaceUrl = reciverFaceUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
