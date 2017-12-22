package com.bingo.vo;


import java.io.Serializable;

public class GroupMember implements Serializable {
    // 群组编号
    private Integer gid;
    // 用户编号
    private Integer uid;

    public GroupMember(Integer gid, Integer uid) {
        this.gid=gid;
        this.uid=uid;
    }

    public GroupMember() {
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
