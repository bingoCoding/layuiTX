package com.bingo.vo;

import com.bingo.domain.User;

import java.util.List;

public class FriendList extends Group {
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
