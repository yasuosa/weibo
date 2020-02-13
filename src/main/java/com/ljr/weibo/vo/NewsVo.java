package com.ljr.weibo.vo;

import com.ljr.weibo.domain.News;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NewsVo extends News {

    private Integer page =1;
    private Integer limit =10;


    //关注对象的id
    private Integer[] focusIds;
}
