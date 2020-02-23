package com.ljr.weibo.controller;


import com.ljr.weibo.utils.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @PostMapping("doUpload")
    @ApiOperation(consumes = "图片上传接口", value = "图片上传接口")
    public Map<String,Object> doUpload(MultipartFile mf){
        String url = uploadService.uploadImage(mf);
        Map<String,Object> map=new HashMap<>();
        map.put("url",url);
        return map;
    }
}
