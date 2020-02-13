package com.ljr.weibo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.service.CommentService;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.utils.AppFileUtils;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.vo.NewsVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;


    /**
     * 文章列表的总查询
     * @param newsVo
     * @return
     */
    @GetMapping("loadAllNews")
    @ApiOperation(consumes = "文章列表的总查询", value = "文章列表的总查询")
    public DataGridView loadAllNews(NewsVo newsVo){
        IPage<News> page=new Page<>(newsVo.getPage(),newsVo.getLimit());
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        //动态查询 关注对象的文章
        queryWrapper.in(newsVo.getFocusIds()!=null,"userid",newsVo.getFocusIds());
        queryWrapper.eq(StringUtils.isNotBlank(newsVo.getType()),"type",newsVo.getType());
        queryWrapper.orderByDesc("newstime");
        newsService.page(page,queryWrapper);
        //分页
        List<News> newsList = page.getRecords();
        QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
        //查询文章对应的图片地址  //查询文章对应的评论数
        for (News news : newsList) {
            news.setImgUrls( newsService.findImgsByNid(news.getNewsid()));
            commentQueryWrapper.eq("nid",newsVo.getNewsid());
            List<Comment> comments = commentService.list(commentQueryWrapper);
            news.setCommentnum(comments.size());
        }
        return new DataGridView(newsList);
    }


    /**
     * 文章添加
     * @param newsVo
     * @return
     */
    @PostMapping("addNews")
    @ApiOperation(consumes = "文章添加", value = "文章添加")
    public ResultObj addNews(NewsVo newsVo){
        newsVo.setNewstime(new Date());
        //设置初始点赞
        newsVo.setLikenum(Constant.DEFAULT_LIKENUM);
        //设置初始转发
        newsVo.setRepeatnum(Constant.DEFAULT_REPEATNUM);
        //设置文章唯一id
        if(newsVo.getType().equals(Constant.NEW_TYPE_MYSELF)) {
            newsVo.setNewsid(SysUtils.getNewsId());
        }
        try {
            newsService.save(newsVo);
            List<String> imgUrls = newsVo.getImgUrls();
            //更改后缀名
            if(null!=imgUrls && imgUrls.size()>0) {
                newsService.saveImgByNid(newsVo.getNewsid(), AppFileUtils.renameFile(imgUrls));
            }
            return ResultObj.INSERT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.INSERT_FAIL;
        }
    }


    /**
     * 删除文章
     * @param newsVo
     * @return
     */
    @PostMapping("deleteNews")
    @ApiOperation(consumes = "删除文章", value = "删除文章")
    public ResultObj deleteNews(NewsVo newsVo){
        String type = newsVo.getType();
        //判断这篇文章是不是作者自己写的
        try {
            if(StringUtils.isNotEmpty(type) && Constant.NEW_TYPE_MYSELF.equals(type)){
                //是的话 有关所有的文章全部删除
                QueryWrapper<News> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("newsid",newsVo.getNewsid());
                //删除 这篇文章有所有关内容 转发
                newsService.remove(queryWrapper);

                QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
                commentQueryWrapper.eq("nid",newsVo.getNewsid());
                //删除评论
                commentService.remove(commentQueryWrapper);
                //删除图片
                //从缓存中找到图片
                List<String> imgs = newsService.findImgsByNid(newsVo.getNewsid());
                newsService.removeImgByNid(newsVo.getNewsid());
                AppFileUtils.removeFile(imgs);
            }else {
                newsService.removeById(newsVo.getId());
            }
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }
}
