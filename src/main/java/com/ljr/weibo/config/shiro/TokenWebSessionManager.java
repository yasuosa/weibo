package com.ljr.weibo.config.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/28
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {
    private static final String TOKEN_HEADER = "TOKEN" ;
    private static final String SOCKET_TOKEN_HEADER = "Sec-WebSocket-Protocol" ;

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String header= WebUtils.toHttp(request).getHeader(TOKEN_HEADER);
        if(StringUtils.isNotBlank(header)){
            return header;
        }

//        System.out.println(WebUtils.toHttp(request).getRequestURL()+"AAAA");
        header=WebUtils.toHttp(request).getHeader(SOCKET_TOKEN_HEADER);
        ((HttpServletResponse)response).addHeader(SOCKET_TOKEN_HEADER,header);
        if(StringUtils.isNotBlank(header)){
            System.out.println(header);
            return header;
        }

        return UUID.randomUUID().toString();
    }


}
