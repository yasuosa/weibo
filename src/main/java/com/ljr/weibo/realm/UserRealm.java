package com.ljr.weibo.realm;


import com.ljr.weibo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;


public class UserRealm extends AuthorizingRealm {


    @Autowired
    @Lazy//懒加载 这里所有的自动注入必须都是懒加载，不然代理对象还是ioc容器最开始的的对象没发动态切入了
    private UserService userService;


    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //用户认证
        String username = (String) token.getPrincipal();
        return null;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //TODO 权限的认证
        return null;
    }


    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
