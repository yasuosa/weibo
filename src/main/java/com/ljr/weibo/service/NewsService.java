package com.ljr.weibo.service;

import com.ljr.weibo.common.DataGridView;
import com.ljr.weibo.common.ResultObj;
import com.ljr.weibo.domain.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljr.weibo.exception.UserIsNotException;
import com.ljr.weibo.vo.BaseVo;
import com.ljr.weibo.vo.NewsVo;

import java.util.List;

public interface NewsService extends IService<News> {


    /**
     * 根据文章id查询 图片地址
     * @param nid
     * @return
     */
   List<String> findImgsByNid(Integer nid);


   List<String> saveImgByNid(Integer nid,List<String> imgUrls);


    boolean removeImgByNid(Integer newsid);


    DataGridView loadNews(NewsVo newsVo,Integer targetUserId) throws UserIsNotException;


    ResultObj likeNews(Integer id) throws UserIsNotException;

    DataGridView  queryNewsById(Integer id) throws UserIsNotException;
}

