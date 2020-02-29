package com.ljr.weibo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ljr.weibo.common.AuthorRepeat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@ApiModel(value = "com.ljr.weibo.domain.News")
@Data
@TableName(value = "sys_news")
public class News {
    /**
     * id  定位id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "id  定位id")
    private Integer id;

    /**
     * 文章id
     */
    @TableField(value = "newsid")
    @ApiModelProperty(value = "文章id")
    private Integer newsid;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "文章内容")
    private String content;

    /**
     * 发布时间
     */
    @TableField(value = "newstime")
    @ApiModelProperty(value = "发布时间")
    private Date newstime;

    /**
     * 作者
     */
    @TableField(value = "author")
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 作者id
     */
    @TableField(value = "userid")
    @ApiModelProperty(value = "作者id")
    private Integer userid;

    /**
     * 喜欢数
     */
    @TableField(value = "likenum")
    @ApiModelProperty(value = "喜欢数")
    private Integer likenum;

    /**
     * 转发数
     */
    @TableField(value = "repeatnum")
    @ApiModelProperty(value = "转发数")
    private Integer repeatnum;

    /**
     * 类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "类型")
    private String type;

    public static final String COL_NEWSID = "newsid";

    public static final String COL_CONTENT = "content";

    public static final String COL_NEWSTIME = "newstime";

    public static final String COL_AUTHOR = "author";

    public static final String COL_USERID = "userid";

    public static final String COL_LIKENUM = "likenum";

    public static final String COL_REPEATNUM = "repeatnum";

    public static final String COL_TYPE = "type";


    //图片地址
    @TableField(exist = false)
    private List<String> imgUrls;

    //评论
    @TableField(exist = false)
    private Integer commentnum;

    //作者头像地址
    @TableField(exist = false)
    private String icon;


    //点赞
    @TableField(exist = false)
    private Boolean isLike=false;

    //层级转发
    @TableField(exist = false)
    private List<AuthorRepeat>repeats=new ArrayList<>();

}