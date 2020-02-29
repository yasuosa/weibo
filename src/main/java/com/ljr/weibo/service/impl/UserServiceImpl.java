package com.ljr.weibo.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.mapper.NewsMapper;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import com.ljr.weibo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Override
    public ResultObj likeUser(Integer userid, Integer likeUserId) {

        Integer count = userMapper.queryRelationship(userid, likeUserId,Constant.RELATIONSHIP_IDOL);
        if(count==0) {
            //你是我们的偶像
            userMapper.insertRelationship(userid, likeUserId, Constant.RELATIONSHIP_IDOL);

            //我是你的粉丝
            userMapper.insertRelationship(likeUserId, userid, Constant.RELATIONSHIP_FAN);
            return new ResultObj(200,"关注成功");
        }
        return new ResultObj(0,"你已经关注它了");
    }

    @Override
    public ResultObj unLikeUser(Integer userid, Integer likeUserId) {
        //你不在是我们的偶像
        userMapper.deleteRelationship(userid, likeUserId, Constant.RELATIONSHIP_IDOL);

        //我也不再是是你的粉丝
        userMapper.deleteRelationship(likeUserId, userid, Constant.RELATIONSHIP_FAN);
        return new ResultObj(200,"取消关注成功");
    }

    @Override
    public ResultObj removeFan(Integer userid, Integer fanid) {
        //你还把我当偶像 我却不把你当回事
        userMapper.deleteRelationship( userid, fanid, Constant.RELATIONSHIP_FAN);
        return new ResultObj(200,"移除粉丝成功");
    }

    @Override
    public ResultObj saveUser(UserVo userVo) {
        try {
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);
            userVo.setImgurl(Constant.DEFAULT_ICON);
            userVo.setPassword(SysUtils.getJoinSaltPwd(salt, userVo.getPassword()));
            userVo.setRegistertime(new Date());
            userVo.setUserid(SysUtils.getUserId());
            userMapper.insert(userVo);
            return ResultObj.REGISTER_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.REGISTER_FAIL;
        }
    }


}


