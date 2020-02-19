package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ljr.weibo.domain.User;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Override
    public boolean updateUserIconByUid(Integer uid, String iconPath) {
        UserMapper userMapper = getBaseMapper();
        try {
            userMapper.updateUserIconByUid(uid,iconPath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String findUserIconByUid(Integer uid) {
        UserMapper userMapper = getBaseMapper();
        return userMapper.findUserIconByUid(uid);
    }
}


