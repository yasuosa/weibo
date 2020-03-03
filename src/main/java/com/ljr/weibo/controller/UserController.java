package com.ljr.weibo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.AuthorRepeat;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.News;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.CommentService;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.AppFileUtils;
import com.ljr.weibo.utils.SysUtils;
import com.ljr.weibo.utils.WeiBoUtils;
import com.ljr.weibo.vo.NewsVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/29
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;
    


    @RequestMapping(value = "repeatNews",method = RequestMethod.POST)
    @ApiOperation(consumes = "转发微博", value = "转发微博")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="索引id",required=true),
    })
    public ResultObj repeatNews(Integer id,String content){
        try {
            News news =newsService.getById(id);
            //转发数加1
            news.setRepeatnum(news.getRepeatnum()+1);
            String author = news.getAuthor();
            newsService.updateById(news);

            //保存转发内容
            //建立层级
            AuthorRepeat authorRepeat=new AuthorRepeat();
            authorRepeat.setContent(content);
            authorRepeat.setUserid(SysUtils.getUser().getUserid());
            authorRepeat.setIcon(SysUtils.getUser().getImgurl());
            authorRepeat.setName(SysUtils.getUser().getUsername());
            news.setAuthor(WeiBoUtils.getAuthorJsonString(author,authorRepeat));

            news.setId(null);
            news.setLikenum(Constant.DEFAULT_LIKENUM);
            news.setRepeatnum(Constant.DEFAULT_REPEATNUM);
            news.setUserid(SysUtils.getUser().getUserid());
            news.setType(Constant.NEW_TYPE_REPEAT);
            news.setNewstime(new Date());
            newsService.save(news);
            return new ResultObj(200,"转发成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(-1,"转发失败");
        }
    }


    @RequestMapping(value = "likeNews",method = RequestMethod.POST)
    @ApiOperation(consumes = "点赞微博", value = "点赞微博")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="索引id",required=true),
    })
    public ResultObj likeNews(Integer id){
        try {
            return  newsService.likeNews(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(-1,"操作失败");
        }
    }

    /**
     * 删除微博
     * @return
     */
    @PostMapping("deleteNews")
    @ApiOperation(consumes = "删除微博", value = "微博删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="索引id",required=true),
            @ApiImplicitParam(name="userid",value="作者id",required=true),
            @ApiImplicitParam(name="newsid",value="文章id",required=true),
            @ApiImplicitParam(name="type",value="文章类型(自己写的/转发)",required=true)
    })
    public ResultObj deleteNews(Integer id,Integer userid,Integer newsid,String type){
        //判断这篇文章是不是作者自己写的
        Integer myId=SysUtils.getUser().getUserid();
        if(!userid.equals(myId)){
            return new ResultObj(-1,"无效删除|没有权限");
        }
        try {
            if(StringUtils.isNotEmpty(type) && Constant.NEW_TYPE_MYSELF.equals(type)){
                //是的话 有关所有的文章全部删除
                QueryWrapper<News> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("newsid",newsid);
                //删除 这篇文章有所有关内容 转发
                newsService.remove(queryWrapper);

                QueryWrapper<Comment> commentQueryWrapper=new QueryWrapper<>();
                commentQueryWrapper.eq("nid",newsid);
                //删除评论
                commentService.remove(commentQueryWrapper);
                //删除图片
                //从缓存中找到图片
                List<String> imgs = newsService.findImgsByNid(newsid);
                if(null!=imgs && imgs.size()!=0) {
                    newsService.removeImgByNid(newsid);
                    AppFileUtils.removeFile(imgs);
                }
            }else {
                newsService.removeById(id);
            }
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_FAIL;
        }
    }

    /**
     *微博发布接口
     * @return
     */
    @RequestMapping(value = "addNews",method = RequestMethod.POST)
    @ApiOperation(consumes = "微博发布接口", value = "微博发布接口")
    public ResultObj addNews(String content,String[] imgs){
        News news=new News();
        if(null !=imgs && imgs.length>0) {
            news.setImgUrls(Arrays.asList(imgs));
        }
        //设置层级作者内容
        AuthorRepeat authorRepeat=new AuthorRepeat();
        authorRepeat.setIndex(Constant.AUTHOR_REPEAT_TOP);
        authorRepeat.setName(SysUtils.getUser().getUsername());
        authorRepeat.setContent("");
        authorRepeat.setUserid(SysUtils.getUser().getUserid());
        authorRepeat.setIcon(SysUtils.getUser().getImgurl());
        List<AuthorRepeat> repeats=new ArrayList<>();
        repeats.add(authorRepeat);
        news.setAuthor(JSON.toJSONString(repeats));

        news.setUserid(SysUtils.getUser().getUserid());
        news.setContent(content);
        news.setType(Constant.NEW_TYPE_MYSELF);
        news.setNewstime(new Date());
        //设置初始点赞
        news.setLikenum(Constant.DEFAULT_LIKENUM);
        //设置初始转发
        news.setRepeatnum(Constant.DEFAULT_REPEATNUM);
        news.setNewsid(SysUtils.getNewsId());
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
     * 查询我自己的微博
     * @return
     */
    @RequestMapping(value = "loadMyNews",method = RequestMethod.GET)
    @ApiOperation(consumes = "查询我自己的微博", value = "查询我自己的微博")
    public DataGridView loadMyNews(Integer page, Integer limit){
        NewsVo newsVo=new NewsVo();
        newsVo.setPage(page);
        newsVo.setLimit(limit);
        newsVo.setSelectType(Constant.SELECT_TYPE_MY);
        return newsService.loadNews(newsVo);
    }

    /**
     * 查询关注的微博

     * @return
     */
    @GetMapping("loadAllNewsByFocus")
    @ApiOperation(consumes = "查询关注的微博", value = "查询关注的微博")
    public DataGridView loadAllNewsByFocus(Integer page, Integer limit){
        NewsVo newsVo=new NewsVo();
        newsVo.setPage(page);
        newsVo.setLimit(limit);
        newsVo.setSelectType(Constant.SELECT_TYPE_MY_FOCUS);
        return newsService.loadNews(newsVo);
    }


    /**
     * 关注用户接口
     * @param likeUserId
     * @return
     */
    @ApiOperation(consumes = "关注用户接口", value = "关注用户接口")
    @RequestMapping(value = "likeUser",method = RequestMethod.POST)
    public ResultObj likeUser(Integer likeUserId){
        Integer userid = SysUtils.getUser().getUserid();
        try {
            return userService.likeUser(userid,likeUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultObj(-1,"关注失败");
        }
    }

    /**
     * 取消关注
     * @param likeUserId
     * @return
     */
    @ApiOperation(consumes = "取消关注接口", value = "取消关注接口")
    @RequestMapping(value = "unLikeUser",method = RequestMethod.POST)
    public ResultObj unLikeUser(Integer likeUserId){
        Integer userid = SysUtils.getUser().getUserid();
        try {
            return userService.unLikeUser(userid,likeUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultObj(-1,"取消关注失败");
        }
    }

    /**
     * 移除粉丝
     * @param likeUserId
     * @return
     */
    @ApiOperation(consumes = "移除粉丝接口", value = "移除粉丝接口")
    @RequestMapping(value = "blockUser",method = RequestMethod.POST)
    public ResultObj removeFan(Integer likeUserId){
        Integer userid = SysUtils.getUser().getUserid();
        try {
            return userService.removeFan(userid,likeUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResultObj(-1,"移除粉丝失败");
        }
    }


    /**
     * 查询粉丝
     */
    @GetMapping("showFans")
    @ApiOperation(consumes = "查询粉丝", value = "查询粉丝")
    public DataGridView showFans(){
        return userService.showFans();
    }

    /**
     * 查询偶像
     */
    @GetMapping("showIdol")
    @ApiOperation(consumes = "查询偶像", value = "查询偶像")
    public DataGridView showIdol(){
        return userService.showIdol();
    }

    /**
     * 个人资料设置
     */
    @GetMapping("modifyUser")
    @ApiOperation(consumes = "修改个人资料", value = "修改个人资料")
    public ResultObj updateUser(User user){
        try {
            QueryWrapper<User> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("userid",user.getUserid());
            userService.update(user,queryWrapper);
            return new ResultObj(200,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(-1,"修改失败");
        }
    }

    /**
     * 查询个人资料
     */
    @GetMapping("showMe")
    @ApiOperation(consumes = "查询个人资料", value = "查询个人资料")
    public DataGridView updateUser(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userid",SysUtils.getUser().getUserid());
        User user = userService.getOne(queryWrapper);
        return new DataGridView(200,"查询成功",null,user);
    }


    /**
     * 查询他人资料
     */
    @GetMapping("showOtherUser")
    @ApiOperation(consumes = "查询他人资料", value = "查询他人资料")
    public DataGridView showOtherUser(Integer targetUserId){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userid",targetUserId);
        User user = userService.getOne(queryWrapper);
        return new DataGridView(200,"查询成功",null,user);
    }


}
