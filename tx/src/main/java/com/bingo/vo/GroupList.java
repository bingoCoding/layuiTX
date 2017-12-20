package com.bingo.vo;

public class GroupList extends Group{
    //群头像地址
    private String avatar;

    //创建者Id
    private Integer createId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }
}
