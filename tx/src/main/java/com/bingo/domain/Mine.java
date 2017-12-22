package com.bingo.domain;

import java.io.Serializable;

public class Mine implements Serializable {
    //我的id
    private int id;

    //我的昵称
    private String username;

    //是否我发的消息
    private Boolean mine;

    //我的头像
    private String avatar;

    //消息内容
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
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
}
