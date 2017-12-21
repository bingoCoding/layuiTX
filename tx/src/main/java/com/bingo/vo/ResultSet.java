package com.bingo.vo;

import com.bingo.common.Contant;

public class ResultSet<T> {
    private int code = Contant.SUCCESS;

    private String msg = Contant.SUCCESS_MESSAGE;

    public T data ;

    public ResultSet() {
    }

    public ResultSet(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultSet(int code, String msg, T data) {
        this(code,msg);
        this.data = data;
    }

    public ResultSet(T data) {
        this.data=data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
