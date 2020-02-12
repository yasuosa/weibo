package com.ljr.weibo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.transform.Result;

/**
 * 返回消息快捷格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {


    public static final ResultObj ERROR =new ResultObj(Constant.CODE_WRONG,Constant.ERROR) ;
    private Integer code;
    private String msg;

    /******登陆*****/
    public static ResultObj LOGIN_SUCCESS=new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS);
    public static ResultObj LOGIN_FAIL=new ResultObj(Constant.CODE_WRONG,Constant.LOGIN_FAIL);
    public static ResultObj LOGIN_WRONG=new ResultObj(Constant.CODE_FLASE,Constant.LOGIN_FLASE);
    public static final ResultObj LOGIN_FAIL_CODE_WRONG = new ResultObj(Constant.CODE_WRONG,Constant.LOGIN_CODE_WRONG);

    /******注册*****/
    public static final ResultObj REGISTER_SUCCESS =new ResultObj(Constant.CODE_TRUE,Constant.REGISTER_SUCCESS);
    public static ResultObj REGISTER_FAIL=new ResultObj(Constant.CODE_WRONG,Constant.REGISTER_FAIL);
}
