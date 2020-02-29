package com.ljr.weibo.vo;

import com.ljr.weibo.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserVo  extends User {

    public UserVo(String loginname, String username, String realname, Integer age, Integer sex, String address, String phone, String email, String password) {
        super(loginname, username, realname, age, sex, address, phone, email, password);
    }
}
