package com.ljr.weibo.utils;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

import java.util.Calendar;

public class SysUtils {


    //散列次数
    private static Integer ITERATIONS=2;


    /**
     * 获取加密后的密码
     * @param salt
     * @param pwd
     * @return
     */
    public static String getJoinSaltPwd(String salt,String pwd){
        return new Md5Hash(pwd,salt,ITERATIONS).toString();
    }

    /**
     * 获取唯一标识userid
     */
    public static Integer getUserId(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int intminutes=cal.get(Calendar.MINUTE);//分
        int intseconds=cal.get(Calendar.SECOND);
        String uuid=""+day+intseconds+month+intminutes+intseconds+ RandomUtil.randomInt(0,9);
        return Integer.parseInt(uuid);
    }


    /**
     * 获取唯一文章标识userid
     */
    public static Integer getNewsId(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int intminutes=cal.get(Calendar.MINUTE);//分
        int intseconds=cal.get(Calendar.SECOND);
        String uuid=""+day+intseconds+month+intminutes+ RandomUtil.randomInt(0,9)+intseconds;
        return Integer.parseInt(uuid);
    }


}
