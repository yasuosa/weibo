package com.ljr.weibo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.service.CommentService;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.AppFileUtils;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 文章列表的总查询
     * @return
     */
    @GetMapping("loadAllNews")
    @ApiOperation(consumes = "微博列表的总查询", value = "微博列表的总查询")
    public DataGridView loadAllNews(Integer page, Integer limit) throws UserIsNotException {
        NewsVo newsVo=new NewsVo();
        newsVo.setPage(page);
        newsVo.setLimit(limit);
        newsVo.setSelectType(Constant.SELECT_TYPE_ALL);
        return  newsService.loadNews(newsVo,null);
    }


    /**
     * 微博的搜索
     * @return
     */
    @GetMapping("searchNews")
    @ApiOperation(consumes = "微博的搜索", value = "微博的搜索")
    public DataGridView searchNews(Integer page, Integer limit,String content) throws UserIsNotException {
        NewsVo newsVo=new NewsVo();
        newsVo.setPage(page);
        newsVo.setLimit(limit);
        newsVo.setContent(content);
        newsVo.setSelectType(Constant.SELECT_TYPE_ALL);
        return  newsService.searchNews(newsVo);
    }


    /**
     * 单个微博搜索
     * @return
     */
    @GetMapping("searchOneNews")
    @ApiOperation(consumes = "单个微博搜索", value = "单个微博搜索")
    public DataGridView  searchOneNew(Integer id) throws UserIsNotException {
        return  newsService.queryNewsById(id);
    }






}
