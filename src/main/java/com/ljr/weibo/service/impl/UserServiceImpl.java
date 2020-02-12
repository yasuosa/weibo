package com.ljr.weibo.service.impl;

import com.ljr.weibo.domain.User;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

