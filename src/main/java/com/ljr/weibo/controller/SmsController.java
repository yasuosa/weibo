package com.ljr.weibo.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.utils.WebUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;

@RestController
@RequestMapping("sms")
public class SmsController {

    @Autowired
    @Lazy
    private MailProperties mailProperties;


    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 像邮箱发送验证码
     * @param sendemail
     */
    @GetMapping("getCode")
    @ApiOperation(consumes = "邮箱发送验证码", value = "邮箱发送验证码")
    public void getCode(String sendemail) throws IOException, MessagingException {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject("微博登陆验证");
        String code = RandomUtil.randomInt(10000, 99999)+"";
        //15分钟
        opsForValue.set(Constant.LOGIN_EMAIL_REDIS_CODE_KEY+sendemail,code, Duration.ofMinutes(Constant.EMAIL_CODE_TIME));
        message.setText("验证码为"+code+";有效期为15分钟");
        message.setTo(sendemail);
        message.setFrom(mailProperties.getUsername());
        mailSender.send(message);
    }

}
