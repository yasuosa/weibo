package com.ljr.weibo.service;

import com.ljr.weibo.common.CommentTree;
import com.ljr.weibo.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {


    List<CommentTree> queryCommentByNid(Integer id);
}

