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
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.AppFileUtils;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @Autowired
    private UserService userService;


    /**
     * 文章列表的总查询
     * @param newsVo
     * @return
     */
    @GetMapping("loadAllNews")
    @ApiOperation(consumes = "文章列表的总查询", value = "文章列表的总查询")
    public DataGridView loadAllNews(NewsVo newsVo){
        return  newsService.loadAllNews(newsVo);
    }

    /**
     * 查询关注的文章
     * @param userid

     * @return
     */
    @GetMapping("loadAllNewsByFocus")
    @ApiOperation(consumes = "文章列表的总查询", value = "文章列表的总查询")
    public DataGridView loadAllNewsByFocus(Integer userid, BaseVo pageVo){
        return newsService.loadAllNewsByFocus(userid,pageVo);
    }



    /**
     * 文章添加
     * @param news
     * @return
     */
    @PostMapping("addNews")
    @ApiOperation(consumes = "文章添加", value = "文章添加")
    public ResultObj addNews(News news){
        news.setNewstime(new Date());
        //设置初始点赞
        news.setLikenum(Constant.DEFAULT_LIKENUM);
        //设置初始转发
        news.setRepeatnum(Constant.DEFAULT_REPEATNUM);
        //设置文章唯一id
        if(news.getType().equals(Constant.NEW_TYPE_MYSELF)) {
            news.setNewsid(SysUtils.getNewsId());
        }
        try {
            newsService.save(news);
            List<String> imgUrls = news.getImgUrls();
            //更改后缀名
            if(null!=imgUrls && imgUrls.size()>0) {
                newsService.saveImgByNid(news.getNewsid(), AppFileUtils.renameFile(imgUrls));
            }
            return ResultObj.INSERT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.INSERT_FAIL;
        }
    }


    /**
     * 删除文章
     * @param news
     * @return
     */
    @PostMapping("deleteNews")
    @ApiOperation(consumes = "删除文章", value = "删除文章")
    public ResultObj deleteNews(News news){
        String type = news.getType();
        //判断这篇文章是不是作者自己写的
        try {
            if(StringUtils.isNotEmpty(type) && Constant.NEW_TYPE_MYSELF.equals(type)){
                //是的话 有关所有的文章全部删除
                QueryWrapper<News> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("newsid",news.getNewsid());
                //删除 这篇文章有所有关内容 转发
                newsService.remove(queryWrapper);

                QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
                commentQueryWrapper.eq("nid",news.getNewsid());
                //删除评论
                commentService.remove(commentQueryWrapper);
                //删除图片
                //从缓存中找到图片
                List<String> imgs = newsService.findImgsByNid(news.getNewsid());
                newsService.removeImgByNid(news.getNewsid());
                AppFileUtils.removeFile(imgs);
            }else {
                newsService.removeById(news.getId());
            }
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }
}
