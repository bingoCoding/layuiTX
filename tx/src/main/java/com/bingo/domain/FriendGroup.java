package com.bingo.domain;

import java.io.Serializable;

public class FriendGroup implements Serializable {
    //用户id
    private Integer uid;

    //群组名称
    private String groupName;

    public FriendGroup(Integer uid, String groupName) {
        this.uid = uid;
        this.groupName = groupName;
    }

    public FriendGroup() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
