package com.ljr.weibo.common;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.classfile.Code;

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
    private Object data;

    public ResultObj(Integer code , String msg){
        this.code=code;
        this.msg=msg;
    }

    /******登陆*****/
    public static ResultObj LOGIN_SUCCESS=new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS);
    public static ResultObj LOGIN_FAIL=new ResultObj(Constant.CODE_WRONG,Constant.LOGIN_FAIL);
    public static ResultObj LOGIN_WRONG=new ResultObj(Constant.CODE_FLASE,Constant.LOGIN_FLASE);
    public static final ResultObj LOGIN_FAIL_CODE_WRONG = new ResultObj(Constant.CODE_WRONG,Constant.LOGIN_CODE_WRONG);

    /******注册*****/
    public static final ResultObj REGISTER_SUCCESS =new ResultObj(Constant.CODE_TRUE,Constant.REGISTER_SUCCESS);
    public static ResultObj REGISTER_FAIL=new ResultObj(Constant.CODE_WRONG,Constant.REGISTER_FAIL);

    /*****文章*****/
    public static final ResultObj INSERT_SUCCESS =new ResultObj(Constant.CODE_TRUE,Constant.INSERT_SUCCESS) ;
    public static final ResultObj INSERT_FAIL =new ResultObj(Constant.CODE_WRONG,Constant.INSERT_FAIL) ;

    public static final ResultObj DELETE_SUCCESS = new ResultObj(Constant.CODE_TRUE, Constant.DELETE_SUCCESS);
    public static final ResultObj DELETE_FAIL = new ResultObj(Constant.CODE_WRONG, Constant.DELETE_FAIL);
}
