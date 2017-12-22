package com.bingo.domain;

import java.io.Serializable;
import java.util.Date;

public class Receive implements Serializable {
    //发送给哪个用户
    private Integer toid;

    //消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
    private Integer id;

    //消息来源用户名
    private String username;

    //消息来源用户头像
    private String avatar;

    //聊天窗口来源类型，从发送消息传递的to里面获取
    private String Type;

    //消息内容
    private String content;

    //消息id，可不传。除非你要对消息进行一些操作（如撤回）
    private int cid;

    //是否我发送的消息，如果为true，则会显示在右方
    private Boolean mine;

    //消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
    private Integer fromid;

    //服务端动态时间戳
    private Date timestamp;

    //消息的状态
    private int status;

    public Integer getToid() {
        return toid;
    }

    public void setToid(Integer toid) {
        this.toid = toid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }

    public Integer getFromid() {
        return fromid;
    }

    public void setFromid(Integer fromid) {
        this.fromid = fromid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
