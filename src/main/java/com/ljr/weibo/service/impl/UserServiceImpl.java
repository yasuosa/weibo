package com.ljr.weibo.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.AuthorRepeat;
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
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;


    @Autowired
    private NewsMapper newsMapper;


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

    @Override
    public DataGridView showFans() {
        List<Integer> fanids=userMapper.queryAimIdByUid(SysUtils.getUser().getUserid(),Constant.RELATIONSHIP_FAN);
        if(null==fanids || fanids.size()==0){
            return new DataGridView(-1,"暂无粉丝！多发点微博试试",null,null);
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("userid",fanids);
        List<User> users = userMapper.selectList(queryWrapper);
        return new DataGridView(200,"查询成功",Long.valueOf(users.size()),users);
    }

    @Override
    public DataGridView showIdol() {
        List<Integer> iodlids=userMapper.queryAimIdByUid(SysUtils.getUser().getUserid(),Constant.RELATIONSHIP_IDOL);
        if(null==iodlids || iodlids.size()==0){
            return new DataGridView(-1,"试试关注他人！",null,null);
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("userid",iodlids);
        List<User> users = userMapper.selectList(queryWrapper);
        return new DataGridView(200,"查询成功",Long.valueOf(users.size()),users);
    }

    @Override
    public DataGridView showMeIndex() {
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        Integer userid = SysUtils.getUser().getUserid();
        queryWrapper.eq("userid", userid);
        User user = userMapper.selectOne(queryWrapper);
        map.put("userid",user.getUserid());
        map.put("sex",user.getSex());
        map.put("name",user.getUsername());
        map.put("content",user.getContent());
        Integer fansNum=userMapper.queryNumFansOrIdols(userid,Constant.RELATIONSHIP_FAN);
        Integer idolsNum=userMapper.queryNumFansOrIdols(userid,Constant.RELATIONSHIP_IDOL);
        map.put("fansNum",fansNum);
        map.put("idolNum",idolsNum);
        QueryWrapper<News> qw=new QueryWrapper<>();
        qw.eq("userid",userid);
        Integer count = newsMapper.selectCount(qw);
        map.put("count",count);
        return new DataGridView(200,"查询成功",null,map);
    }

    @Override
    public DataGridView showOthersIndex(Integer uid) {
        Map<String,Object> map=new HashMap<>();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        Integer myId = SysUtils.getUser().getUserid();
        queryWrapper.eq("userid", uid);
        User user = userMapper.selectOne(queryWrapper);
        map.put("userid",user.getUserid());
        map.put("sex",user.getSex());
        map.put("name",user.getUsername());
        map.put("content",user.getContent());
        Integer fansNum=userMapper.queryNumFansOrIdols(uid,Constant.RELATIONSHIP_FAN);
        Integer idolsNum=userMapper.queryNumFansOrIdols(uid,Constant.RELATIONSHIP_IDOL);
        map.put("fansNum",fansNum);
        map.put("idolNum",idolsNum);
        //判断是不是我的关注
        Integer i1 = userMapper.queryRelationship(myId, uid, Constant.RELATIONSHIP_IDOL);
        //判断是不是我的粉丝
        Integer i2 = userMapper.queryRelationship(myId, uid, Constant.RELATIONSHIP_FAN);
        map.put("isIdol",i1==0?false:true);
        map.put("isFan",i2==0?false:true);
        QueryWrapper<News> qw=new QueryWrapper<>();
        qw.eq("userid",uid);
        Integer count = newsMapper.selectCount(qw);
        map.put("count",count);
        return new DataGridView(200,"查询成功",null,map);
    }


    @Override
    public boolean update(User entity, Wrapper<User> updateWrapper) {
        //根性author里面的json字符串
        Integer userid=entity.getUserid();
        List<News> news = newsMapper.selectList(null);
        for (News n : news) {
            String author = n.getAuthor();
            List<AuthorRepeat> repeats= JSON.parseArray(author,AuthorRepeat.class);
            for (AuthorRepeat repeat : repeats) {
                if(repeat.getUserid().equals(userid)){
                    repeat.setName(entity.getUsername());
                }
            }
            n.setAuthor(JSON.toJSONString(repeats));
            newsMapper.updateById(n);
        }
        return super.update(entity,updateWrapper);
    }


}


