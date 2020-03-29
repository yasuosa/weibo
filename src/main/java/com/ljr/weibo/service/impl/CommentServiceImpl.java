package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljr.weibo.common.CommentTree;
import com.ljr.weibo.common.Constant;
import com.ljr.weibo.domain.User;
import com.ljr.weibo.mapper.UserMapper;
import com.ljr.weibo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljr.weibo.domain.Comment;
import com.ljr.weibo.mapper.CommentMapper;
import com.ljr.weibo.service.CommentService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public List<CommentTree> queryCommentByNid(Integer id) {
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("nid",id);
        queryWrapper.orderByAsc("time");
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        List<CommentTree> commentTreeList=new ArrayList<>();
        for (Comment comment : comments) {
            QueryWrapper<User> qw=new QueryWrapper<>();
            qw.eq("userid",comment.getUid());
            User user = userMapper.selectOne(qw);
            commentTreeList.add(
                    new CommentTree(
                            comment.getId(),
                            comment.getPid(),
                            comment.getUid(),
                            user.getUsername(),
                            user.getImgurl(),
                            comment.getTime(),
                            comment.getContent()
                    )
            );
        }
        return CommentTree.CommentTreeBuilder.build(commentTreeList, Constant.COMMENT_TOP);
    }
}

