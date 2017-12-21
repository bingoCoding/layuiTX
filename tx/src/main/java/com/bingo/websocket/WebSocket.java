package com.bingo.websocket;

import com.bingo.common.Contant;
import com.bingo.domain.Message;
import com.bingo.service.Impl.RedisService;
import com.bingo.utils.SpringManage;
import com.bingo.utils.WebSocketUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

/**
 * @description websocket服务器处理消息
 */
@ServerEndpoint(value = "/websocket/{uid}")
@Component
public class WebSocket {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);

    private  RedisService redisService = SpringManage.getBean(RedisService.class);

    private final Gson gson  = new Gson();

    private Integer uid;

    /**
     * @description 服务器接收到消息调用
     * @param message 消息体
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session) {
        Message mess = gson.fromJson(message.replaceAll("type", "Type"), Message.class);
        LOGGER.info("来自客户端的消息: " + mess);
        switch (mess.getType()){
            case "message" :{
                WebSocketUtil.sendMessage(mess);
                break;
            }
            case "checkOnline" : {
                Map<String, String> result = WebSocketUtil.checkOnline(mess, session);
                WebSocketUtil.sendMessage(gson.toJson(result), session);
                break;
            }
            case "addGroup" : {
                WebSocketUtil.addGroup(uid, mess);
                break;
            }
            case "changOnline" : {
                WebSocketUtil.changeOnline(uid, mess.getMsg());
                break;
            }
            case "addFriend" : {
                WebSocketUtil.addFriend(uid, mess);
                break;
            }
            case "agreeAddFriend" : {
                if (WebSocketUtil.sessions.get(mess.getTo().getId()) != null) {
                    WebSocketUtil.sendMessage(message, WebSocketUtil.sessions.get(mess.getTo().getId()));
                }
                break;
            }
            case "agreeAddGroup" : {
                WebSocketUtil.agreeAddGroup(mess);
                break;
            }
            case "refuseAddGroup" : {
                WebSocketUtil.refuseAddGroup(mess);
                break;
            }
            case "unHandMessage" : {
                Map<String, String> result = WebSocketUtil.countUnHandMessage(uid);
                WebSocketUtil.sendMessage(gson.toJson(result), session);
                break;
            }
            case "delFriend" : {
                WebSocketUtil.removeFriend(uid, mess.getTo().getId());
                break;
            }
            default: {
                LOGGER.info("No Mapping Message!");
                break;
            }
        }
    }

    /**
     * @description 首次创建链接
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid")Integer uid) {
        this.uid = uid;
        WebSocketUtil.sessions.put(uid, session);
        LOGGER.info("userId = " + uid + ",sessionId = " + session.getId() + ",新连接加入!");
        redisService.setSet(Contant.ONLINE_USER, uid + "");
    }

    /**
     * @description 链接关闭调用
     * @param session
     */
    @OnClose
    public void onClose(Session session)  {
        LOGGER.info("userId = " + uid + ",sessionId = " + session.getId() + "断开连接!");
        WebSocketUtil.sessions.remove(uid);
        redisService.removeSetValue(Contant.ONLINE_USER, uid + "");
    }

    /**
     * @description 服务器发送错误调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session,Throwable error ) {
        LOGGER.info(new StringBuilder().append(session.getId()).append(" 发生错误").append(error.getStackTrace()).toString());
        onClose(session);
    }
}
