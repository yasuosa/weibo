package com.ljr.weibo.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.domain.UserLikeNews;
import com.ljr.weibo.mapper.UserLikeNewsMapper;
import com.ljr.weibo.service.UserLikeNewsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserLikeNewsServiceImpl extends ServiceImpl<UserLikeNewsMapper, UserLikeNews> implements UserLikeNewsService{

}
