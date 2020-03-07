package com.ljr.weibo.utils;

import cn.hutool.core.util.RandomUtil;
import com.ljr.weibo.common.ActiviUser;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.exception.UserIsNotException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

public class SysUtils {


    //散列次数
    private static Integer ITERATIONS=2;

    private static Log log= LogFactory.getLog(SysUtils.class.getSimpleName());


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
        int month = cal.get(Calendar.MONTH) + 1;
        int intminutes=cal.get(Calendar.MINUTE);//分
        int intseconds=cal.get(Calendar.SECOND);
        String uuid=""+intseconds+month+intminutes+intseconds+ RandomUtil.randomInt(0,9);
        return Integer.parseInt(uuid);
    }


    /**
     * 获取唯一文章标识userid
     */
    public static Integer getNewsId(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int intminutes=cal.get(Calendar.MINUTE);//分
        int intseconds=cal.get(Calendar.SECOND);
        String uuid=""+intseconds+month+intminutes+ RandomUtil.randomInt(0,9)+intseconds;
        return Integer.parseInt(uuid);
    }


    /**
     * 获取ActiviUser
     */
    public static ActiviUser getActiviUser() throws UserIsNotException {
        ActiviUser activiUser = (ActiviUser) SecurityUtils.getSubject().getPrincipal();
        if(null ==activiUser){
            throw new UserIsNotException(-1,"请先登陆|token失效");
        }
        return activiUser;
    }


    /**
     * 得到session里面的user
     * @return
     */
    public static User getUser() throws UserIsNotException {
        return getActiviUser().getUser();
    }


    /**
     * 得到邮箱样式
     * @return
     * @throws IOException
     */
    public static String buildContent(String code) throws IOException {

        //加载邮件html模板
        String fileName = "pod-scale-alarm.html";
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.error("读取文件失败，fileName:{}"+ fileName);
        } finally {
            inputStream.close();
            fileReader.close();
        }


        String contentText = "以下是登陆的验证码, 敬请查看.<br> <font size=\"24px\"><u>"+code+"</u></font> 当前有效时间为15分钟。";
        //邮件表格header
        String header = "<td>分区(Namespace)</td><td>服务(Service)</td><td>伸缩结果(Scale Result)</td><td>伸缩原因(Scale Reason)</td><td>当前实例数(Pod instance number)</td>";
        StringBuilder linesBuffer = new StringBuilder();
        linesBuffer.append("<tr><td>" + "myNamespace" + "</td><td>" + "myServiceName" + "</td><td>" + "myscaleResult" + "</td>" +
                "<td>" + "mReason" + "</td><td>" + "my4" + "</td></tr>");

        //绿色
        String emailHeadColor = "#10fa81";
        String date = DateFormatUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss");
        //填充html模板中的五个参数
        String htmlText = MessageFormat.format(buffer.toString(), emailHeadColor, contentText, date, header, linesBuffer.toString());

        //改变表格样式
        htmlText = htmlText.replaceAll("<td>", "<td style=\"padding:6px 10px; line-height: 150%;\">");
        htmlText = htmlText.replaceAll("<tr>", "<tr style=\"border-bottom: 1px solid #eee; color:#666;\">");
        return htmlText;
    }



}
