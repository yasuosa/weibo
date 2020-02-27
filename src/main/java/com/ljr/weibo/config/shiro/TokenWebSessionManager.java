package com.ljr.weibo.config.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/28
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {
    private static final String TOKEN_HEADER = "TOKEN" ;

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String header= WebUtils.toHttp(request).getHeader(TOKEN_HEADER);
        if(StringUtils.isNotBlank(header)){
            return header;
        }
        return UUID.randomUUID().toString();
    }
}
