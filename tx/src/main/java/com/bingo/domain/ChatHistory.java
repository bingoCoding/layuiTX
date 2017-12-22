package com.bingo.domain;

import java.io.Serializable;
import java.util.Date;

public class ChatHistory implements Serializable {
    //用户id
    private Integer id;

    //用户名
    private String username;

    //用户头像
    private String avatar;

    //消息内容
    private String content;

    //时间
    private Date timestamp;

    public ChatHistory(Integer id, String username, String avatar, String content, Date timestamp) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatHistory() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
