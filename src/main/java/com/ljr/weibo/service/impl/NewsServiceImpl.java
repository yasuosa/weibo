package com.ljr.weibo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.AuthorRepeat;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.UserLikeNews;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.mapper.CommentMapper;
import com.ljr.weibo.mapper.UserLikeNewsMapper;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.NewsMapper;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.service.NewsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@Transactional(rollbackFor = Exception.class)
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {


    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserLikeNewsMapper userLikeNewsMapper;

    @Override
    @Cacheable(cacheNames = "img:news:",key = "#nid")
    public List<String> findImgsByNid(Integer nid) {
        NewsMapper newsMapper = getBaseMapper();
        List<String> imgs = newsMapper.queryListImgByNid(nid);
        if(imgs.size()>0){
            return imgs;
        }
        return null;
    }

    @Override
    @CachePut(cacheNames = "img:news:",key = "#nid")
    public List<String> saveImgByNid(Integer nid, List<String> imgUrls) {
        NewsMapper newsMapper = getBaseMapper();
        try {
            newsMapper.insertImgAndNid(nid, imgUrls);
            return imgUrls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @CacheEvict(cacheNames = "img:news:",key = "#newsid")
    public boolean removeImgByNid(Integer newsid) {
        NewsMapper newsMapper = getBaseMapper();
        return newsMapper.deleteImgAndNid(newsid);
    }


    public DataGridView loadNews(NewsVo newsVo,Integer tatgetUserId) throws UserIsNotException {

        IPage<News> page=new Page<>(newsVo.getPage(),newsVo.getLimit());
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        //动态查询内容
        String content = newsVo.getContent();
        queryWrapper.like(StringUtils.isNotBlank(content),"content",content);

        queryWrapper.orderByDesc("newstime");
        Integer userid=null;
        if(SecurityUtils.getSubject().isAuthenticated()){
            userid= SysUtils.getUser().getUserid();
        }
        if(Constant.SELECT_TYPE_MY.equals(newsVo.getSelectType()) && null==tatgetUserId){
            queryWrapper.eq("userid",userid);
        }
        if(tatgetUserId!=null){
            queryWrapper.eq("userid",tatgetUserId);
        }
        //查询当前用户的关注者
        if(Constant.SELECT_TYPE_MY_FOCUS.equals(newsVo.getSelectType())){
            List<Integer> focusIds=userMapper.queryOtherUserByUidAndType(userid, Constant.RELATIONSHIP_IDOL);
            //动态查询 关注对象的文章
            if(null ==focusIds || focusIds.size()==0){
                return new DataGridView(200,"没有关注对象，逛一逛！",null,null);
            }
            queryWrapper.in( "userid", focusIds);
        }
        newsMapper.selectPage(page,queryWrapper);
        //分页
        List<News> newsList = page.getRecords();
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        //查询文章对应的图片地址  //查询文章对应的评论数
        for (News news : newsList) {
            if(userid!=null){
                Boolean isLike=userMapper.queryMyLikeByUidAndNid(userid,news.getId())==0?false:true;
                news.setIsLike(isLike);
            }
            news.setImgUrls(findImgsByNid(news.getNewsid()));
            commentQueryWrapper.eq("nid",news.getNewsid());
            List<Comment> comments = commentMapper.selectList(commentQueryWrapper);
            news.setCommentnum(comments.size());
            //头像地址
            String icon = userMapper.findUserIconByUid(news.getUserid());
            List<AuthorRepeat> repeats = JSON.parseArray(news.getAuthor(), AuthorRepeat.class);
            news.setRepeats(repeats);
            news.setIcon(icon);
        }
        return new DataGridView(200,"查询成功",page.getTotal(),page.getRecords());
    }

    @Override
    public ResultObj likeNews(Integer id) throws UserIsNotException {
        News news =newsMapper.selectById(id);
        Integer userid = SysUtils.getUser().getUserid();

        QueryWrapper<UserLikeNews> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("nid",id);
        queryWrapper.eq("uid",userid);
        UserLikeNews userLike= userLikeNewsMapper.selectOne(queryWrapper);
        if(null==userLike) {
            news.setLikenum(news.getLikenum() + 1);
            newsMapper.updateById(news);
            UserLikeNews userLikeNews=new UserLikeNews();
            userLikeNews.setNid(id);
            userLikeNews.setUid(userid);
            userLikeNews.setTime(new Date());
            userLikeNewsMapper.insert(userLikeNews);
            return new ResultObj(200,"点赞成功");
        }else {
            news.setLikenum(news.getLikenum()-1);
            newsMapper.updateById(news);
            userLikeNewsMapper.deleteById(userLike.getId());
            return new ResultObj(200,"取消点赞成功");
        }
    }


}

