package com.ljr.weibo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.CommentService;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.SysUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/1
 */
@RestController
@RequestMapping("comment")
public class CommentController {



    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;



    /**
     * 查询单条微博所有评论
     */
    @GetMapping("showComment")
    @ApiOperation(consumes = "查询单条微博所有评论", value = "查询单条微博所有评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="nid",value="微博索引的id",required=true),
    })
    public DataGridView showComment(Integer nid){
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("nid",nid);
        List<Comment> comments = commentService.list(queryWrapper);
        if(null ==comments ||comments.size()==0){
            return new DataGridView(-1,"尚未有人评论！试着曝光！",null,null);
        }
        QueryWrapper<User> qw=new QueryWrapper<>();
        for (Comment comment : comments) {
            qw.eq("userid",comment.getUid());
            User user = userService.getOne(qw);
            comment.setUser(user);
        }
        return new DataGridView(200,"查询成功",Long.valueOf(comments.size()),comments);
    }





    /**
     * 评论微博
     */
    @GetMapping("commentNews")
    @ApiOperation(consumes = "评论微博", value = "评论微博")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="文章索引id(不是newsid)",required=true),
            @ApiImplicitParam(name="content",value="评论内容",required=true),
    })
    public ResultObj commentNews(Integer id, String content){
        try {
            Comment comment=new Comment();
            comment.setNid(id);
            comment.setContent(content);
            comment.setLikenum(Constant.DEFAULT_LIKENUM);
            comment.setNolikenum(Constant.DEFAULT_UNLIKENUM);
            comment.setTime(new Date());
            comment.setUid(SysUtils.getUser().getUserid());
            commentService.save(comment);
            return new ResultObj(200,"评论成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(-1,"评论失败");
        }
    }


    /**
     * 删除评论
     */
    @GetMapping("deleteComment")
    @ApiOperation(consumes = " 删除评论", value = " 删除评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="评论id",required=true),
    })
    public ResultObj deleteComment(Integer id){
        try {
            commentService.removeById(id);
            return new ResultObj(200,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(-1,"删除失败");
        }

    }



}
