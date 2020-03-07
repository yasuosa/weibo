package com.ljr.weibo.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mail.UserPassAuthenticator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.common.TokenResult;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.utils.WebUtils;
import com.ljr.weibo.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 开发测试用的登陆接口 不需要验证码
     *
     * @param loginname
     * @param password
     * @return
     */
    @PostMapping("loginDebug")
    @ApiOperation(consumes = "开发测试用的登陆接口 不需要验证码", value = "开发测试用的登陆接口 不需要验证码")
    public TokenResult loginDebug(String loginname, String password) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginname, password);
            subject.login(usernamePasswordToken);
            String token= subject.getSession().getId().toString();
            return new TokenResult(200,"登陆成功",token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new TokenResult(-1,"账户密码错误",null);
        }
    }


    /**
     * 用户账号密码登陆
     *
     * @param loginname
     * @param password
     * @return
     */
    @Deprecated
    @PostMapping("login")
    @ApiOperation(consumes = "用户账号密码登陆", value = "用户账号密码登陆")
    public TokenResult login(String loginname, String password, String keyCode,String code) {
        if (StringUtils.isBlank(code)) {
            return new TokenResult(-1,"请填写验证码",null);
        }
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String oldCode= (String) opsForValue.get(Constant.LOGIN_REDIS_CODE_KEY + keyCode);
        if(StringUtils.isBlank(oldCode)) {
            return new TokenResult(-1,"验证码失效|请重新获取",null);
        }
        if(!code.equals(oldCode)){
            return new TokenResult(-1,"验证码错误",null);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginname, password);
            subject.login(usernamePasswordToken);
            String token= subject.getSession().getId().toString();
            redisTemplate.delete(Constant.LOGIN_REDIS_CODE_KEY + keyCode);
            return new TokenResult(200,"登陆成功",token,SysUtils.getUser());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new TokenResult(-1, "账户密码错误", null);
        } catch (UserIsNotException e) {
            e.printStackTrace();
            return new TokenResult(-1, e.getMsg(), null);
        }
    }


    /**
     * 注册
     *
     * @param userVo
     * @return
     */
    @PostMapping("toRegister")
    @ApiOperation(consumes = "用户账号密码注册", value = "用户账号密码注册")
    public ResultObj toRegister(UserVo userVo) {
        String username = userVo.getUsername();
        if(StringUtils.isBlank(username)){
            userVo.setUsername(UUID.randomUUID()+"R");
        }
        return userService.saveUser(userVo);
    }

    /**
     * 获取验证码
     *
     * @throws IOException
     */
    @GetMapping("getCode")
    @ApiOperation(consumes = "获得验证码", value = "获得验证码")
    public void getCode(String keyCode) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 5);
        //得到code
        String code = lineCaptcha.getCode();
        System.out.println(code);
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        //验证码一个分钟有效
        opsForValue.set(Constant.LOGIN_REDIS_CODE_KEY + keyCode,code, Duration.ofMinutes(Constant.IMG_CODE_TIME));
        ServletOutputStream out = WebUtils.getResponse().getOutputStream();
        lineCaptcha.write(out);
        out.close();
    }


    /**
     * 邮箱登陆
     * @param email
     * @param code
     * @return
     */
    @PostMapping("loginByEmail")
    @ApiOperation(consumes = "邮箱登陆", value = "邮箱登陆")
    public TokenResult loginByEmail(String email,String code){
        if(StringUtils.isBlank(code)) {
            return new TokenResult(-1,"请填写验证码",null);
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken passwordToken = new UsernamePasswordToken(email, code);
        try {
            subject.login(passwordToken);
            String token = subject.getSession().getId().toString();
            redisTemplate.delete(Constant.LOGIN_EMAIL_REDIS_CODE_KEY +email);
            return new TokenResult(200,"登陆成功",token,SysUtils.getUser());
        } catch(AuthenticationException e) {
            e.printStackTrace();
            return new TokenResult(-1,"验证码失效|请重新获取",null);
        } catch (UserIsNotException e) {
            e.printStackTrace();
            return new TokenResult(-1, e.getMsg(), null);
        }
    }


    /**
     * 退出登陆
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    @ApiOperation(consumes = "退出登陆", value = "退出登陆")
    public void  logout(){
        SecurityUtils.getSubject().logout();
    }


}
