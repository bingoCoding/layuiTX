package com.bingo.vo;

import com.bingo.domain.User;

import java.io.Serializable;
import java.util.List;

public class FriendList extends Group implements Serializable {
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
