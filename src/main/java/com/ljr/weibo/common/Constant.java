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
    String INSERT_SUCCESS ="发布成功" ;
    String INSERT_FAIL ="发布失败" ;
    Integer DEFAULT_LIKENUM = 0;
    Integer DEFAULT_REPEATNUM = 0;

    /**文章类型*/
    String NEW_TYPE_MYSELF = "myself";
    String NEW_TYPE_REPEAT= "repeat";
    String DELETE_SUCCESS = "删除成功";
    String DELETE_FAIL= "删除失败";

    //默认头像
    String DEFAULT_ICON = "/default/default.jpg";


    /**人际关系图 */
    String RELATIONSHIP_IDOL = "idol";
    String RELATIONSHIP_FAN = "fan";

    /**验证码的key*/
    String LOGIN_REDIS_CODE_KEY = "login:code:";
    String LOGIN_EMAIL_REDIS_CODE_KEY = "login:email:code:";

    /**验证码的有效时间*/
    Long IMG_CODE_TIME =  1L; //图像
    Long EMAIL_CODE_TIME =  15L; //邮箱
}
