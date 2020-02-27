package com.ljr.weibo.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo {

    @ApiModelProperty(value = "前端分页 页码 默认1")
    private Integer page=1; //前端分页 页码
    @ApiModelProperty(value = "前端分页 条数 默认10")
    private Integer limit=10;//前端分页 页数
}
