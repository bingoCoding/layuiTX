package com.bingo.utils;

import com.bingo.common.Contant;
import com.bingo.domain.*;
import com.bingo.service.Impl.RedisService;
import com.bingo.service.Impl.UserService;
import com.bingo.websocket.domain.Domain;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class WebSocketUtil {
    private final static Logger LOGER  = LoggerFactory.getLogger(WebSocketUtil.class);

    public final static Map<Integer,Session> sessions = Collections.synchronizedMap(new HashMap<>());

    private static RedisService redisService = SpringManage.getBean(RedisService.class);

    private static UserService userService = SpringManage.getBean(UserService.class);

    private final static Gson gson = new Gson();

    /**
     * @description 发送消息
     * @message Message
     */
    public static synchronized void sendMessage(Message message) {
        LOGER.info("发送好友消息和群消息!");
        //封装返回消息格式
        Integer gid = message.getTo().getId();
        Receive receive = WebSocketUtil.getReceiveType(message);
        Integer key = message.getTo().getId();
        //聊天类型，可能来自朋友或群组
        if("friend".equals(message.getTo().getType())) {
            //是否在线
            if(WebSocketUtil.sessions.containsKey(key)) {
                Session session = WebSocketUtil.sessions.get(key);
                receive.setStatus(1);
                WebSocketUtil.sendMessage(gson.toJson(receive).replaceAll("Type", "type"), session);
            }
            //保存为离线消息,默认是为离线消息
            userService.saveMessage(receive);
        } else {
            receive.setId(gid);
            //找到群组id里面的所有用户
            List<User> users = userService.findUserByGroupId(gid);
            //过滤掉本身的uid
            users.stream().forEach(user -> {
                if (user.getId() != message.getMine().getId()){
                    //是否在线
                    if(WebSocketUtil.sessions.containsKey(user.getId())) {
                        Session session = WebSocketUtil.sessions.get(user.getId());
                        receive.setStatus(1);
                        WebSocketUtil.sendMessage(gson.toJson(receive).replaceAll("Type", "type"), session);
                    } else {
                        receive.setId(key);
                    }
                }
            });
            //保存为离线消息
            userService.saveMessage(receive);
        }
    }

    /**
     * @description 同意添加好友
     * @param mess
     */
    public static void agreeAddGroup(Message mess) {
        Domain.AgreeAddGroup agree = gson.fromJson(mess.getMsg(), Domain.AgreeAddGroup.class);
        userService.addGroupMember(agree.getGroupId(), agree.getToUid(), agree.getMessageBoxId());
    }

    /**
     * @description 拒绝添加群
     * @param mess
     */
    public static void refuseAddGroup(Message mess) {
        Domain.AgreeAddGroup refuse = gson.fromJson(mess.getMsg(), Domain.AgreeAddGroup.class);
        userService.updateAddMessage(refuse.getMessageBoxId(), 2);
    }

    /**
     * @description 通知对方删除好友
     * @param uId 我的id
     * @param friendId 对方Id
     */
    public static synchronized void removeFriend(Integer uId, Integer friendId) {
        //对方是否在线，在线则处理，不在线则不处理
        Map<String,String> result = new HashMap<>();
        if(sessions.get(friendId) != null) {
            result.put("type", "delFriend");
            result.put("uId", uId + "");
            WebSocketUtil.sendMessage(gson.toJson(result), sessions.get(friendId));
        }
    }

    /**
     * @description 添加群组
     * @param uid
     * @param message
     */
    public static synchronized void addGroup(Integer uid, Message message) {
        AddMessage addMessage = new AddMessage();
        Mine mine = message.getMine();
        To to = message.getTo();
        Domain.Group t = gson.fromJson(message.getMsg(), Domain.Group.class);
        addMessage.setFromUid(mine.getId());
        addMessage.setToUid(to.getId());
        addMessage.setTime(new Date());
        addMessage.setGroupId(t.getGroupId());
        addMessage.setRemark(t.getRemark());
        addMessage.setType(1);
        userService.saveAddMessage(addMessage);
        Map<String,String> result = new HashMap<>();
        if (sessions.get(to.getId()) != null) {
            result.put("type", "addGroup");
            sendMessage(gson.toJson(result), sessions.get(to.getId()));
        }
    }

    /**
     * @description 添加好友
     * @param uid
     * @param message
     */
    public static synchronized void addFriend(Integer uid, Message message) {
        Mine mine = message.getMine();
        AddMessage addMessage = new AddMessage();
        addMessage.setFromUid(mine.getId());
        addMessage.setTime(new Date());
        addMessage.setToUid(message.getTo().getId());
        Add add = gson.fromJson(message.getMsg(), Add.class);
        addMessage.setRemark(add.getRemark());
        addMessage.setType(add.getType());
        addMessage.setGroupId(add.getGroupId());
        userService.saveAddMessage(addMessage);
        Map<String,String> result = new HashMap<>();
        //如果对方在线，则推送给对方
        if (sessions.get(message.getTo().getId()) != null) {
            result.put("type", "addFriend");
            sendMessage(gson.toJson(result), sessions.get(message.getTo().getId()));
        }
    }

    /**
     * @description 统计离线消息数量
     * @param uid
     */
    public static synchronized Map<String, String> countUnHandMessage(Integer uid) {
        Integer count = userService.countUnHandMessage(uid, 0);
        LOGER.info("count = " + count);
        Map<String, String> result = new HashMap<>();
        result.put("type", "unHandMessage");
        result.put("count", count + "");
        return result;
    }

    /**
     * @description 监测某个用户的离线或者在线
     * @param message
     */
    public static synchronized Map<String, String> checkOnline(Message message, Session session)  {
        LOGER.info("监测在线状态" + message.getTo().toString());
        Set<String> uids = redisService.getSets(Contant.ONLINE_USER);
        Map<String,String> result = new HashMap<>();
        result.put("type", "checkOnline");
        if (uids.contains(message.getTo().getId().toString()))
            result.put("status", "在线");
        else
            result.put("status", "离线");
        return result;
    }

    /**
     * @description 发送消息
     * @param message
     * @param session
     */
    public static synchronized void sendMessage(String message, Session session) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 封装返回消息格式
     * @param Message
     * @return Receive
     */
    static Receive getReceiveType(Message message) {
        Mine mine=message.getMine();
        To to = message.getTo();
        Receive receive = new Receive();
        receive.setId(mine.getId());
        receive.setFromid(mine.getId());
        receive.setToid(to.getId());
        receive.setUsername(mine.getUsername());
        receive.setType(to.getType());
        receive.setAvatar(mine.getAvatar());
        receive.setContent(mine.getContent());
        receive.setTimestamp(new Date());
        return receive;
    }

    /**
     * @description 用户在线切换状态
     * @param uid 用户id
     * @param status 状态
     */
    public static synchronized void changeOnline(Integer uid, String status) {
        if ("online".equals(status)) {
            redisService.setSet(Contant.ONLINE_USER, uid + "");
        } else {
            redisService.removeSetValue(Contant.ONLINE_USER, uid + "");
        }
    }
}
