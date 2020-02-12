package com.ljr.weibo.common;

/**
 * 基础数据文本
 */
public interface Constant {

    /***
     * 响应返回值
     */
    Integer CODE_TRUE=200;
    Integer CODE_FLASE=0;
    Integer CODE_WRONG=-1;

    /**
     * 文本信息
     */
    String LOGIN_SUCCESS="登陆成功";
    String LOGIN_FAIL="登陆失败!密码错误";
    String LOGIN_FLASE="登陆失败!当前用户未注册！";
    String REGISTER_SUCCESS="注册成功";
    String REGISTER_FAIL="注册失败!用户名已存在！";
    String LOGIN_CODE_WRONG = "验证码错误";


    String DEFAULT_PASSWORD_SUTFF="123456";


    String ERROR = "错误错误!";
}
