package com.ljr.weibo.service.impl;

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
    public List<String> findImgsByNid(Integer nid) {
        NewsMapper newsMapper = getBaseMapper();
        return newsMapper.queryListImgByNid(nid);
    }

    @Override
    public boolean saveImgByNid(Integer nid, List<String> imgUrls) {
        NewsMapper newsMapper = getBaseMapper();
        try {
            newsMapper.insertImgAndNid(nid, imgUrls);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeImgByNid(Integer newsid) {
        NewsMapper newsMapper = getBaseMapper();
        return newsMapper.deleteImgAndNid(newsid);
    }


}

