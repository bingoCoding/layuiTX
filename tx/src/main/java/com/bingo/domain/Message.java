package com.bingo.domain;

import java.io.Serializable;

public class Message implements Serializable {
    //随便定义，用于在服务端区分消息类型
    private String Type;

    //我的信息
    private Mine mine;

    //对方信息
    private To to;

    //额外的信息
    private String msg;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Mine getMine() {
        return mine;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
