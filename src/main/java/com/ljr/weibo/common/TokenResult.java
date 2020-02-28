package com.ljr.weibo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/28
 */

@Data
@AllArgsConstructor
public class TokenResult {

    //状态码
    private Integer code;
    //得到消息
    private String msg;

    //token嘛
    private String token;

    public TokenResult(Integer code, String msg, String token) {
        this.code = code;
        this.msg = msg;
        this.token = token;
    }

    private Object data;
}
