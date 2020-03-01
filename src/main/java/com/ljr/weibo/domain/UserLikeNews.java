package com.ljr.weibo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@ApiModel(value="com.ljr.weibo.domain.UserLikeNews")
@Data
@TableName(value = "sys_user_like")
public class UserLikeNews {
    /**
     * 主键id
     */
     @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="主键id")
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "uid")
    @ApiModelProperty(value="用户id")
    private Integer uid;

    /**
     * 文章id
     */
    @TableField(value = "nid")
    @ApiModelProperty(value="文章id")
    private Integer nid;

    /**
     * 创建时间
     */
    @TableField(value = "time")
    @ApiModelProperty(value="创建时间")
    private Date time;

    public static final String COL_UID = "uid";

    public static final String COL_NID = "nid";

    public static final String COL_TIME = "time";
}