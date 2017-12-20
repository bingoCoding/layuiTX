package com.bingo.domain;

public class AddFriends {
    //自己的id
    private int mid;
    //分组id
    private int mgid;
    //对方用户id
    private int tid;
    //对方分组id
    private int tgid;

    public AddFriends(int mid, int mgid, int tid, int tgid) {
        this.mid = mid;
        this.mgid = mgid;
        this.tid = tid;
        this.tgid = tgid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getMgid() {
        return mgid;
    }

    public void setMgid(int mgid) {
        this.mgid = mgid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTgid() {
        return tgid;
    }

    public void setTgid(int tgid) {
        this.tgid = tgid;
    }
}
