package com.ljr.weibo.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.ljr.weibo.utils.WebUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SmsController {

    @Autowired
    @Lazy
    private MailProperties mailProperties;


    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 像邮箱发送验证码
     * @param sendemail
     */
    @GetMapping("getCode")
    @ApiOperation(consumes = "邮箱发送验证码", value = "邮箱发送验证码")
    public void getCode(String sendemail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("微博登陆验证");
        String code = RandomUtil.randomInt(10000, 99999)+"";
        WebUtils.getSession().setAttribute("smsCode",code);
        message.setText("验证码："+ code);
        message.setTo(sendemail);
        message.setFrom(mailProperties.getUsername());
        mailSender.send(message);
    }
}
