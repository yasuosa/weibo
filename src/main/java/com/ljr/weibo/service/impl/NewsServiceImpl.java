package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.mapper.CommentMapper;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.mapper.NewsMapper;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.service.NewsService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {


    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

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


    @Override
    public DataGridView loadAllNews(NewsVo newsVo) {
        IPage<News> page=new Page<>(newsVo.getPage(),newsVo.getLimit());
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        //动态查询 关注对象的文章
        if(null!=newsVo.getFocusIds() && newsVo.getFocusIds().length>0) {
            queryWrapper.in( "userid", newsVo.getFocusIds());
        }
        queryWrapper.orderByDesc("newstime");
        newsMapper.selectPage(page,queryWrapper);
        //分页
        List<News> newsList = page.getRecords();
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        //查询文章对应的图片地址  //查询文章对应的评论数
        for (News news : newsList) {
            news.setImgUrls(findImgsByNid(news.getNewsid()));
            commentQueryWrapper.eq("nid",news.getNewsid());
            List<Comment> comments = commentMapper.selectList(commentQueryWrapper);
            news.setCommentnum(comments.size());
            //头像地址
            String icon = userMapper.findUserIconByUid(news.getUserid());
            news.setIcon(icon);
        }
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 查询关注的文章总数
     * @param userid
     * @param pageVo
     * @return
     */
    @Override
    public DataGridView loadAllNewsByFocus(Integer userid, BaseVo pageVo) {
        //查询当前用户的关注者
        List<Integer> aids=userMapper.queryOtherUserByUidAndType(userid, Constant.RELATIONSHIP_IDOL);
        return null;
    }

}

