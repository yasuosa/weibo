package com.ljr.weibo.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.AppFileUtils;
import com.ljr.weibo.utils.WebUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private UserService userService;


    /***
     * 头像上传 地址相对地址 位 用户userid/imgname.jpg
     * @param mf
     * @return
     */
    @PostMapping("uploadIcon")
    @ApiOperation(consumes = "头像图片上传接口", value = "头像图片上传接口")
    public Map<String,Object> uploadIcon(Integer userid,MultipartFile mf){
        String oldname = mf.getOriginalFilename();
        String newName= AppFileUtils.createNewName(oldname);
        String dirName= (userid)+"";
        File dirFile=new File(AppFileUtils.UPLOAD_PATH_IMG,dirName);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File file=new File(dirFile,newName);
        Map<String,Object> map=new HashMap<>();
        try {
            mf.transferTo(file);
            //更改用户头像
            userService.updateUserIconByUid(userid,dirName+"/"+newName);
            map.put("value",dirName+"/"+newName);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("value","img is not success upload!");
        }
        return map;
    }

    /**
     * 图片上传接口
     * 注意 未提交整个表单之前 图片以temp后缀形式
     * @param mf
     * @return
     */
    @PostMapping("uploadImg")
    @ApiOperation(consumes = "图片上传接口", value = "图片上传接口")
    public Map<String,Object> uploadImg(MultipartFile mf){
        List<String> imgPaths=new ArrayList<>();
        String oldname = mf.getOriginalFilename();
        String newName= AppFileUtils.createNewNameTemp(oldname);
        String dirName= DateUtil.format(new Date(),"yyyy-MM-dd");
        File dirFile=new File(AppFileUtils.UPLOAD_PATH_IMG,dirName);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File file=new File(dirFile,newName);
        Map<String,Object> map=new HashMap<>();
        try {
            mf.transferTo(file);
            imgPaths.add(dirName+"/"+newName);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("value","img is not success upload!");
        }
        map.put("value",imgPaths);
        return map;
    }



    /**
     * 文件下载
     * @param path
     * @return
     */
    @GetMapping("showImgByPath")
    @ApiOperation(consumes = "图片下载接口", value = "图片下载接口")
    public ResponseEntity<Object> showImg(String path){
        return AppFileUtils.createResponseEntity(path);
    }
}
