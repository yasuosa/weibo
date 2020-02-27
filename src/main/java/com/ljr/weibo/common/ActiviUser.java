package com.ljr.weibo.common;

import com.ljr.weibo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiviUser implements Serializable {

    private User user;

    //角色
    private List<String> roles;

    //权限
    private List<String> permissions;
}
