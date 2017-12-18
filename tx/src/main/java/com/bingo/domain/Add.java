package com.bingo.domain;

import java.util.Date;

public class Add {
    //好友列表id或群组id
    private Integer groupId;
    //附言
    private String remark;
    //类型，好友或群组
    private int Type;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
