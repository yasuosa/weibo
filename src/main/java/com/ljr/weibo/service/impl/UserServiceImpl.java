package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ljr.weibo.domain.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Override
    @CachePut(cacheNames = "img:icon:",key = "#uid")
    public String updateUserIconByUid(Integer uid, String iconPath) {
        UserMapper userMapper = getBaseMapper();
        try {
            userMapper.updateUserIconByUid(uid,iconPath);
            return iconPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Cacheable(cacheNames = "img:icon:",key = "#uid")
    public String findUserIconByUid(Integer uid) {
        UserMapper userMapper = getBaseMapper();
        return userMapper.findUserIconByUid(uid);
    }
}


