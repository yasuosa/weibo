package com.ljr.weibo.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.utils.WebUtils;
import com.ljr.weibo.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;


    /**
     * 开发测试用的登陆接口 不需要验证码
     *
     * @param loginname
     * @param password
     * @return
     */
    @PostMapping("loginDebug")
    @ApiOperation(consumes = "开发测试用的登陆接口 不需要验证码", value = "开发测试用的登陆接口 不需要验证码")
    public ResultObj loginDebug(String loginname, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname", loginname);
        User user = userService.getOne(queryWrapper);
        if (null == user) {
            return ResultObj.LOGIN_WRONG;
        }
        String userPwd = user.getPassword();
        String salt = user.getSalt();
        if (userPwd.equals(SysUtils.getJoinSaltPwd(salt, password))) {
            WebUtils.getSession().setAttribute("user", user);
            WebUtils.getSession().removeAttribute("code");
            return new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS,user);
        } else {
            return ResultObj.LOGIN_FAIL;
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
    public ResultObj login(String loginname, String password, String code) {
        //先不做认证了 最后在做 不然不好测试
        if (null != code) {
            if (!WebUtils.getSession().getAttribute("code").equals(code)) {
                return ResultObj.LOGIN_FAIL_CODE_WRONG;
            }
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("loginname", loginname);
            User user = userService.getOne(queryWrapper);
            if (null == user) {
                return ResultObj.LOGIN_WRONG;
            }
            String userPwd = user.getPassword();
            String salt = user.getSalt();
            if (userPwd.equals(SysUtils.getJoinSaltPwd(salt, password))) {
                WebUtils.getSession().setAttribute("user", user);
                WebUtils.getSession().removeAttribute("code");
                return new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS,user);
            } else {
                return ResultObj.LOGIN_FAIL;
            }
        } else {
            return ResultObj.LOGIN_FAIL_CODE_WRONG;
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
        try {
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);
            userVo.setImgurl(Constant.DEFAULT_ICON);
            userVo.setPassword(SysUtils.getJoinSaltPwd(salt, userVo.getPassword()));
            userVo.setRegistertime(new Date());
            userVo.setUserid(SysUtils.getUserId());
            userService.save(userVo);
            return ResultObj.REGISTER_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.REGISTER_FAIL;
        }
    }

    /**
     * 获取验证码
     *
     * @throws IOException
     */
    @GetMapping("getCode")
    @ApiOperation(consumes = "获得验证码", value = "获得验证码")
    public void getCode() throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 5);
        //得到code
        String code = lineCaptcha.getCode();
        System.out.println(code);
        WebUtils.getSession().setAttribute("code", code);
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
    public ResultObj loginByEmail(String email,String code){
        if(null!=code){
            if(WebUtils.getSession().getAttribute("smsCode").equals(code)){
                QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
                userQueryWrapper.eq("email",email);
                User user = userService.getOne(userQueryWrapper);
                if(null==user){
                    //自动注册
                    user=new User();
                    user.setLoginname(email);
                    user.setUserid(SysUtils.getUserId());
                    user.setImgurl(Constant.DEFAULT_ICON);
                    String salt = IdUtil.simpleUUID().toUpperCase();
                    user.setSalt(salt);
                    user.setPassword(SysUtils.getJoinSaltPwd(
                            salt,
                            email+ Constant.DEFAULT_PASSWORD_SUTFF
                    ));
                    user.setRegistertime(new Date());
                    user.setEmail(email);
                    try {
                        userService.save(user);
                        WebUtils.getSession().removeAttribute("smsCode");
                        return new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS,user);
                    } catch (Exception e){
                        e.printStackTrace();
                        return ResultObj.ERROR;
                    }
                }else {
                    WebUtils.getSession().setAttribute("user",user);
                    return new ResultObj(Constant.CODE_TRUE,Constant.LOGIN_SUCCESS,user);
                }
            }else {
                return ResultObj.LOGIN_FAIL_CODE_WRONG;
            }
        }else {
            return ResultObj.LOGIN_FAIL_CODE_WRONG;
        }
    }
}
