package com.ljr.weibo.controller;

import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.socket.WebSocketServer;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * @Auther 任鹏宇
 * @Date 2020/4/1
 */

@RestController
public class SocketController {

    @GetMapping("notice")
    @ApiOperation(consumes = "通知", value = "通知")
    public ResultObj like(String toId,String msg) throws IOException {
        WebSocketServer.sendInfo(msg,toId);
        return ResultObj.YES;
    }
}
