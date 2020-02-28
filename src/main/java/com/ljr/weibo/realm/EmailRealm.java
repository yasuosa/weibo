package com.ljr.weibo.realm;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.ActiviUser;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.SysUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/28
 */

@Component
public class EmailRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private StringRedisTemplate redisTemplate;

    @Autowired
    @Lazy
    private UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String email = (String) authenticationToken.getPrincipal();
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String code = opsForValue.get(Constant.LOGIN_EMAIL_REDIS_CODE_KEY + email);
        if(StringUtils.isBlank(code)) {
            return null;
        }
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("email",email);
        User user = userService.getOne(userQueryWrapper);
        ActiviUser activiUser=new ActiviUser();
        if(null==user) {
            //自动注册
            user = new User();
            user.setLoginname(email);
            user.setUserid(SysUtils.getUserId());
            user.setImgurl(Constant.DEFAULT_ICON);
            String salt = IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);
            user.setPassword(SysUtils.getJoinSaltPwd(
                    salt,
                    email + Constant.DEFAULT_PASSWORD_SUTFF
            ));
            user.setRegistertime(new Date());
            user.setEmail(email);
            userService.save(user);
        }
        activiUser.setUser(user);
        AuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(activiUser,code,this.getName());
        return authenticationInfo;
    }
}
