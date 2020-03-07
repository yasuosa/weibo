package com.ljr.weibo.exception;

import lombok.Data;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/7
 */
@Data
public class UserIsNotException extends Exception {
    private Integer code;
    private String msg;

    public UserIsNotException(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
