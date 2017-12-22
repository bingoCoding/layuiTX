package com.bingo.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class SecurityUtil {
    /** 秘钥*/
    private final static String SITE_WIDE_SECRET = "bingo";

    private final static PasswordEncoder encoder = new StandardPasswordEncoder(SITE_WIDE_SECRET);

    /**
     * @description 采用SHA-256算法，迭代1024次，使用一个密钥(site-wide secret)以及8位随机盐对原密码进行加密
     * @param rawPassword
     * @return 80位加密后的密码
     */
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * @description 验证密码和加密后密码是否一致
     * @param rawPassword 明文密码
     * @param password 加密后的密码
     * @return
     */
    public static Boolean matchs(String rawPassword,String password) {
        if (rawPassword == null && password == null) {
            return true;
        }
        return encoder.matches(rawPassword, password);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
