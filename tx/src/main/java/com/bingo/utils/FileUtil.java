package com.bingo.utils;

import com.bingo.common.Contant;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileUtil {
    /**
     * @description 文件保存服务器
     * @param types 文件类型/upload/image 或  /upload/file
     * @param path 文件绝对路径地址
     * @param file 二进制文件
     * @return 文件的相对路径地址
     */
    String upload(String types ,String path,MultipartFile file){
        String name = file.getOriginalFilename();
        String paths = path + types + DateUtils.formatDate(new Date()) + "/";
        String result = types + DateUtils.formatDate(new Date()) + "/";
        //如果是图片，则使用uuid重命名图片
        if (Contant.IMAGE_PATH.equals(types)) {
            name = UUIDUtil.getUUID32String() + name.substring(name.indexOf("."));
        } else if (Contant.FILE_PATH.equals(types)) {
            //如果是文件，则区分目录
            String p = UUIDUtil.getUUID32String();
            paths = paths + p;
            result += p + "/";
        }
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(paths, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result + name;
    }

    /**
     * @description 头像
     * @param realpath 服务器绝对路径地址
     * @param file 文件
     * @return 相对路径
     */
    String upload(String realpath ,MultipartFile file ) {
        String name = file.getOriginalFilename();
        name = UUIDUtil.getUUID32String() + name.substring(name.indexOf("."));
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realpath, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Contant.AVATAR_PATH + name;
    }
}
