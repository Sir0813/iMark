/*
package com.dm.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/api/chat/{userId}")
public class WebSocketChatServer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketChatServer.class);

    */
/**
 * 在线用户
 * <p>
 * 离线消息暂时存储
 * PS:重启服务消息会丢失
 * <p>
 * 建立连接
 *
 * @param session
 * @param userId
 * @throws IOException
 * <p>
 * 发送消息
 * @param session
 * @param jsonStr
 * @throws Exception
 * 对方在线 立即发送消息  对方离线 暂时存储消息至服务器 (服务重启信息丢失)  新增第一条离线消息  追加消息
 * 关闭连接
 * @param session
 * @param userId
 * <p>
 * 连接异常
 * @param session
 * @param error
 *//*

    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    */
/**
 * 离线消息暂时存储
 * PS:重启服务消息会丢失
 *//*

    private static Map<String, List<String>> userMap = new HashMap<>();

    */
/**
 * 建立连接
 *
 * @param session
 * @param userId
 * @throws IOException
 *//*

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId) throws IOException {
        onlineSessions.put(String.valueOf(userId), session);
        if (null != userMap.get(String.valueOf(userId))) {
            List<String> strings = userMap.get(String.valueOf(userId));
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                onlineSessions.get(String.valueOf(userId)).getBasicRemote().sendText(s);
            }
            userMap.remove(String.valueOf(userId));
        }
        logger.info("websocket连接成功<" + userId + ">加入!!!");
    }

    */
/**
 * 发送消息
 *
 * @param session
 * @param jsonStr
 * @throws Exception
 *//*

    @OnMessage
    public void onMessage(Session session, String jsonStr) throws Exception {
        try {
            Message message = JSONObject.parseObject(jsonStr, Message.class);
            Integer userId = message.getReciverId();
            message.setCreateTime(DateUtil.getSystemTimeStr());
            message.setOnlineCount(onlineSessions.size());
            String json = JSON.toJSONString(message);
            if (null != onlineSessions.get(String.valueOf(userId))) {
                */
/** 对方在线 立即发送消息 **//*

                onlineSessions.get(String.valueOf(userId)).getBasicRemote().sendText(json);
            } else {
                */
/** 对方离线 暂时存储消息至服务器 (服务重启信息丢失) **//*

                if (null == userMap.get(String.valueOf(userId))) {
                    */
/** 新增第一条离线消息 **//*

                    List<String> msg = new ArrayList<>(16);
                    msg.add(json);
                    userMap.put(String.valueOf(userId), msg);
                } else {
                    */
/** 追加消息 **//*

                    List<String> strings = userMap.get(String.valueOf(userId));
                    strings.add(json);
                }
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    */
/**
 * 关闭连接
 *
 * @param session
 * @param userId
 *//*

    @OnClose
    public void onClose(Session session, @PathParam("userId") int userId) {
        onlineSessions.remove(String.valueOf(userId));
        logger.info("websocket连接关闭<" + userId + ">退出!!!");
    }

    */
/**
 * 连接异常
 *
 * @param session
 * @param error
 *//*

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("websocket连接异常", error);
    }

    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
*/
