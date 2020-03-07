package com.ljr.weibo.config;

import com.ljr.weibo.exception.UserIsNotException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/7
 */
@RestControllerAdvice
public class GolbalExpectionHandler  {

    /**
     * shiro测试接口 session值娶不到 返回的结果 等一版结束 添加shiro认证路径 则改掉
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {UserIsNotException.class})
    public Map<String,Object> notUser(UserIsNotException e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        map.put("code",e.getCode());
        map.put("msg",e.getMsg());
        return map;
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public Map<String,Object> nullValue(NullPointerException e, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        map.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        map.put("msg","为空值");
        map.put("aim","定位地址为:"+e.getStackTrace()[0].getClassName()+"/"+e.getStackTrace()[0].getMethodName());
        return map;
    }
}
