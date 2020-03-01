package com.ljr.weibo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="com.ljr.weibo.domain.Comment")
@Data
@TableName(value = "sys_news_comment")
public class Comment {
     @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="null")
    private Integer id;

    @TableField(value = "nid")
    @ApiModelProperty(value="null")
    private Integer nid;

    @TableField(value = "uid")
    @ApiModelProperty(value="null")
    private Integer uid;

    @TableField(value = "content")
    @ApiModelProperty(value="null")
    private String content;

    @TableField(value = "time")
    @ApiModelProperty(value="null")
    private Date time;

    @TableField(value = "likenum")
    @ApiModelProperty(value="null")
    private Integer likenum;

    @TableField(value = "nolikenum")
    @ApiModelProperty(value="null")
    private Integer nolikenum;

    public static final String COL_NID = "nid";

    public static final String COL_UID = "uid";

    public static final String COL_CONTENT = "content";

    public static final String COL_TIME = "time";

    public static final String COL_LIKENUM = "likenum";

    public static final String COL_NOLIKENUM = "nolikenum";


    @TableField(exist = false)
    private User user;
}