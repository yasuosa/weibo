package com.ljr.weibo.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ljr.weibo.common.ActiviUser;
import com.ljr.weibo.domain.SocketMsg;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.utils.SysUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/31
 */
@ServerEndpoint("/ims")
@Component
public class WebSocketServer {

    public  static Log log = LogFactory.getLog(WebSocketServer.class.getSimpleName());

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static Integer onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<Object, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userid
     */
    private String userid="";

    private String username="";


    /**消息的存储**/
    public static Map<Object,Object> msgMap=new HashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws IOException, UserIsNotException {
        this.session = session;
        String name = session.getUserPrincipal().getName();
        if(StringUtils.isBlank(name)){
            throw  new UserIsNotException(-1,"登陆先");
        }
        this.userid =SysUtils.getWebSocketUserId(name);
        this.username=SysUtils.getWebSocketUserName(name);

        if (webSocketMap.containsKey(this.userid)) {
            webSocketMap.remove(this.userid);
            webSocketMap.put(this.userid, this);
            //加入set中
        } else {
            webSocketMap.put(this.userid, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        if(msgMap.containsKey(this.userid)){
            log.info("用户userid"+ this.userid +"存有未读信息");
            Map<Object,Object> map= (Map<Object, Object>) msgMap.get(this.userid);
            for(Map.Entry<Object, Object> entry : map.entrySet()){
                //从服务端 发送客户端
                sendMessage(entry.getKey()+":"+entry.getValue());
                map.remove(entry.getKey());
                log.info("删除了--->"+entry.getKey()+"---"+entry.getValue());
            }
        }

        log.info("用户连接:" + this.userid + ",当前在线人数为:" + getOnlineCount());

        SocketMsg socketMsg=new SocketMsg();
        socketMsg.setType("SPEAK");
        socketMsg.setFromId(userid);
        socketMsg.setUsername(username);
        socketMsg.setTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
        socketMsg.setMsg("连接成功");
        socketMsg.setOnlineCount(getOnlineCount());
        try {
            sendMessage(JSON.toJSONString(socketMsg));
        } catch (IOException e) {
            log.error("用户:" + this.userid + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userid)) {
            webSocketMap.remove(userid);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userid + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userid + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromId", this.userid);
                String toUserId = jsonObject.getString("toId");
                jsonObject.put("username",this.username);
                jsonObject.put("type","SPEAK");
                jsonObject.put("time",DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"));
                jsonObject.put("onlineCount",getOnlineCount());
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的userid:" + toUserId + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                    Map<Object,Object> map;
                    if(msgMap.containsKey(userid)){
                        map= (Map<Object, Object>) msgMap.get(userid);
                    }else {
                        map=new HashMap<>();
                    }
                    map.put(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"),message);
                    msgMap.put(userid,map);
                    log.error("用户" + userid + ",不在线！消息存入msgMap");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userid + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 实现服务器的群发
     * @param ids
     * @param message
     * @throws IOException
     */
    public void sendAllCientMsg(List<String> ids,String message) throws IOException {
        for (String id : ids) {
            if (webSocketMap.containsKey(id)) {
                webSocketMap.get(id).sendMessage(message);
            }
        }
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userid") String userid) throws IOException {
        log.info("发送消息到:" + userid + "，报文:" + message);
        if (StringUtils.isNotBlank(userid) && webSocketMap.containsKey(userid)) {
            webSocketMap.get(userid).sendMessage(message);
        } else {
            Map<Object,Object> map;
            if(msgMap.containsKey(userid)){
               map= (Map<Object, Object>) msgMap.get(userid);
            }else {
                map=new HashMap<>();
            }
            map.put(DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss"),message);
            msgMap.put(userid,map);
            log.error("用户" + userid + ",不在线！消息存入msgMap");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

