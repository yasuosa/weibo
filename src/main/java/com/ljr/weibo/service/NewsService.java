package com.ljr.weibo.service;

import com.ljr.weibo.domain.News;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface NewsService extends IService<News> {





    /**
     * 根据文章id查询 图片地址
     * @param nid
     * @return
     */
   List<String> findImgsByNid(Integer nid);


    boolean saveImgByNid(Integer nid,List<String> imgUrls);


    boolean removeImgByNid(Integer newsid);
}

