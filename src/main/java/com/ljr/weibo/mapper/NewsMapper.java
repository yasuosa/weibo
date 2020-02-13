package com.ljr.weibo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljr.weibo.domain.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsMapper extends BaseMapper<News> {


    List<String> queryListImgByNid(Integer nid);

    void insertImgAndNid(@Param("nid") Integer nid, @Param("imgs") List<String> imgUrls);

    boolean deleteImgAndNid(Integer newsid);
}