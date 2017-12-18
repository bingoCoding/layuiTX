package com.bingo.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class User {
    private Integer id;

    //用户名
    @NotNull
    private String username;

    //密码
    @NotNull
    private String password;

    //签名
    @NotNull
    private String sign;

    //头像
    private String avatar;

    //邮箱
    @NotNull
    private String email;

    //创建时间
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    //性别
    private Integer sex;

    //状态
    private String status;

    //激活码
    private String active;
}
