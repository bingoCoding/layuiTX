package com.bingo.domain;

import java.util.Date;

public class AddMessage {
    private Integer id;
    //谁发起的请求
    private Integer fromUid;
    //发送给谁的申请,可能是群，那么就是创建该群组的用户
    private Integer toUid;
    //如果是添加好友则为from_id的分组id，如果为群组则为群组id
    private Integer groupId;
    //附言
    private String remark;
    //0未处理，1同意，2拒绝
    private int agree;
    //类型，可能是添加好友或群组
    private int Type;
    //申请时间
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromUid() {
        return fromUid;
    }

    public void setFromUid(Integer fromUid) {
        this.fromUid = fromUid;
    }

    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
    }

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

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
