package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.mapper.NewsMapper;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;




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


