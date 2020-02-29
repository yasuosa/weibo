package com.ljr.weibo.utils;


import com.alibaba.fastjson.JSON;
import com.ljr.weibo.common.ActiviUser;
import com.ljr.weibo.common.AuthorRepeat;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/29
 */
public class WeiBoUtils {

    /**
     * 转发的数据格式
     * @return
     */
    public static String getAuthorJsonString(String dbBeforeJson, AuthorRepeat repeat){
        List<AuthorRepeat> authorRepeatList=new ArrayList<>();
        if(StringUtils.isNotBlank(dbBeforeJson)){
            authorRepeatList= (List<AuthorRepeat>) JSON.parseArray(dbBeforeJson, AuthorRepeat.class);
        }
        repeat.setIndex(authorRepeatList.size()+1);
        authorRepeatList.add(repeat);
        return JSON.toJSONString(authorRepeatList);
    }
}
