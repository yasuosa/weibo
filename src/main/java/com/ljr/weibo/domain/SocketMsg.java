package com.ljr.weibo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther 任鹏宇
 * @Date 2020/4/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMsg implements Serializable {
    private String fromId;
    private String toId;
    private String username;
    private String msg;
    private String type;
    private String time;
    private Integer onlineCount;
}
