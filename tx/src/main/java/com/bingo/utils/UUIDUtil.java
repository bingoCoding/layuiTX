package com.bingo.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * @description 64位随机UUID
     * @return String
     */
    public static String getUUID64String(){
        return (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "");
    }

    /**
     * @description 32位随机UUID
     * @return String
     */
    public static String getUUID32String(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
