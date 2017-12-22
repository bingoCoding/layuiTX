package com.bingo.vo;

import java.io.Serializable;

public class Group implements Serializable {
    private Integer id;
    private String groupname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
