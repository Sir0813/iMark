package com.dm.user.entity;

import com.alibaba.fastjson.JSON;

public class Message {

    public static final String ENTER = "ENTER";
    public static final String SPEAK = "SPEAK";
    public static final String QUIT = "QUIT";

    private UserInfoMsg sender;

    private UserInfoMsg reciver;

    private int type;//消息类型  0 文字 1 图片

    private String msg; //发送消息

    private String createTime;//发送时间 消息时间格式(yyyy-MM-dd HH:mm:ss)

    private int onlineCount; //在线用户数

    public static String jsonStr(int type, String msg, String createTime, int onlineCount) {
        return JSON.toJSONString(new Message(type, msg, createTime, onlineCount));
    }

    public Message(int type, String msg, String createTime, int onlineCount) {
        this.type = type;
        this.msg = msg;
        this.createTime = createTime;
        this.onlineCount = onlineCount;
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

    public UserInfoMsg getSender() {
        return sender;
    }

    public void setSender(UserInfoMsg sender) {
        this.sender = sender;
    }

    public UserInfoMsg getReciver() {
        return reciver;
    }

    public void setReciver(UserInfoMsg reciver) {
        this.reciver = reciver;
    }
}
