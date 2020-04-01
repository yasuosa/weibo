package com.ljr.weibo.socket;

import com.ljr.weibo.utils.SysUtils;
import lombok.SneakyThrows;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/31
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();

        return serverEndpointExporter;
    }

    /**
     * 修改握手,就是在握手协议建立之前修改其中携带的内容
     * @param sec
     * @param request
     * @param response
     */
    @SneakyThrows
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        sec.getUserProperties().put("user", SysUtils.getUser());
        super.modifyHandshake(sec, request, response);

    }
}
