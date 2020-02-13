package com.ljr.weibo.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 文件工具类
 */
public class AppFileUtils {


    public static String UPLOAD_PATH_IMG="E:/weibo/img/";

    static {
        InputStream in = AppFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
        Properties properties=new Properties();
        try {
            properties.load(in);
            UPLOAD_PATH_IMG=properties.getProperty("uploadImgUrl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *创建新的缓存名字
     * @param oldname
     * @return
     */
    public static String createNewNameTemp(String oldname){
        String stuff=oldname.substring(oldname.lastIndexOf("."),oldname.length());
        return IdUtil.simpleUUID().toUpperCase()+stuff+"_temp";
    }


    /**
     *创建新的名字
     * @param oldname
     * @return
     */
    public static String createNewName(String oldname){
        String stuff=oldname.substring(oldname.lastIndexOf("."),oldname.length());
        return IdUtil.simpleUUID().toUpperCase()+stuff;
    }


    /**
     * 文件传输到前端页面
     * @param path
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        File file=new File(UPLOAD_PATH_IMG,path);
        if(file.exists()){
            byte[] bytes=null;
            bytes= FileUtil.readBytes(file);
            HttpHeaders httpHeaders=new HttpHeaders();
            //封装响应内容类型
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //文件下载名称
            httpHeaders.setContentDispositionFormData("attchment",path.replace('/', '-'));
            ResponseEntity<Object> responseEntity=new ResponseEntity<>(bytes,httpHeaders, HttpStatus.CREATED);
            return responseEntity;
        }
        return null;
    }

    /**
     * 删除文件
     * @param imgUrls
     */
    public static void removeFile(List<String> imgUrls) {
        if(null==imgUrls ||imgUrls.size()==0)return;
        for (String imgUrl : imgUrls) {
            File file=new File(UPLOAD_PATH_IMG,imgUrl);
            if(file.exists()){
                file.delete();
            }
        }
    }


    /**
     * 将temp中间图片 改成正确图片格式
     * @param imgUrls
     * @return
     */
    public static List<String>  renameFile(List<String> imgUrls) {
        List<String> imgs=new ArrayList<>();
        for (String imgUrl : imgUrls) {
            File file=new File(UPLOAD_PATH_IMG,imgUrl.trim());
            if(file.exists()){
                String trueImgUrl = imgUrl.replace("_temp", "");
                imgs.add(trueImgUrl);
                file.renameTo(new File(UPLOAD_PATH_IMG, trueImgUrl));
            }
        }
        System.out.println("AAAA"+imgs);
        return imgs;
    }
}
