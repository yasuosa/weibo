package com.ljr.weibo.vo;

import com.ljr.weibo.domain.News;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class NewsVo extends BaseVo{
    //关注对象的id
    @ApiModelProperty(value = "关注对象的id")
    private Integer[] focusIds;
}
