package com.ljr.weibo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljr.weibo.domain.User;

public interface UserService extends IService<User> {


    /**
     * 跟换头像地址
     * @param uid
     * @param iconPath
     * @return
     */
    boolean updateUserIconByUid(Integer uid,String iconPath);


    /**
     * 查询用户头像
     * @param uid
     * @return
     */
    String findUserIconByUid(Integer uid);

}


