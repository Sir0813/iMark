package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;

public interface BizMsgService {

    Result msgList() throws Exception;

    Result msgDetails(String chatId, String createTime) throws Exception;
}
