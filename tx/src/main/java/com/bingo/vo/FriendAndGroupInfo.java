package com.bingo.vo;

import com.bingo.domain.User;

import java.io.Serializable;
import java.util.List;

public class FriendAndGroupInfo implements Serializable {
    //我的信息
    private User mine;

    //好友列表
    private List<FriendList> friend;

    //群组分组
    private List<GroupList> group;

    public User getMine() {
        return mine;
    }

    public void setMine(User mine) {
        this.mine = mine;
    }

    public List<FriendList> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendList> friend) {
        this.friend = friend;
    }

    public List<GroupList> getGroup() {
        return group;
    }

    public void setGroup(List<GroupList> group) {
        this.group = group;
    }
}
