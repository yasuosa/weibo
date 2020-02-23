package com.ljr.weibo.service.impl;

import com.baomidou.mybatisplus.extension.api.Assert;
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


    @Override
    @Cacheable(cacheNames = "img:news:",key = "#nid", condition = "#relust.size()>0")
    public List<String> findImgsByNid(Integer nid) {
        NewsMapper newsMapper = getBaseMapper();
        List<String> imgs = newsMapper.queryListImgByNid(nid);
        if(imgs.size()>0){
            return imgs;
        }
        return null;
    }

    @Override
    @CachePut(cacheNames = "img:news:",key = "#nid",condition = "#imgUrls.size()>0")
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


}

