package com.ljr.weibo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "com.ljr.weibo.domain.User")
@Data
@TableName(value = "sys_user")
@AllArgsConstructor
@NoArgsConstructor
public class User  implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户id")
    private Integer id;

    /**
     * 用户唯一标识
     */
    @TableId(value = "userid", type = IdType.INPUT)
    @ApiModelProperty(value = "用户唯一标识")
    private Integer userid;

    /**
     * 登陆名称
     */
    @TableField(value = "loginname")
    @ApiModelProperty(value = "登陆名称")
    private String loginname;

    /**
     * 用户名字
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名字")
    private String username;

    /**
     * 真实名字
     */
    @TableField(value = "realname")
    @ApiModelProperty(value = "真实名字")
    private String realname;

    /**
     * 年龄
     */
    @TableField(value = "age")
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * -1未知 0女 1难
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "-1未知 0女 1难")
    private Integer sex;

    /**
     * 用户地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "用户地址")
    private String address;

    /**
     * 电话 可作为登陆账户
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "电话 可作为登陆账户")
    private String phone;

    /**
     * 邮箱可作为 登陆账户
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "邮箱可作为 登陆账户")
    private String email;

    /**
     * 注册时间
     */
    @TableField(value = "registertime")
    @ApiModelProperty(value = "注册时间")
    private Date registertime;

    /**
     * 加密的salt
     */
    @TableField(value = "salt")
    @ApiModelProperty(value = "加密的salt")
    private String salt;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 用户图片地址
     */
    @TableField(value = "imgurl")
    @ApiModelProperty(value = "用户图片地址 ")
    private String imgurl;

    public static final String COL_LOGINNAME = "loginname";

    public static final String COL_USERNAME = "username";

    public static final String COL_REALNAME = "realname";

    public static final String COL_AGE = "age";

    public static final String COL_SEX = "sex";

    public static final String COL_ADDRESS = "address";

    public static final String COL_PHONE = "phone";

    public static final String COL_EMAIL = "email";

    public static final String COL_REGISTERTIME = "registertime";

    public static final String COL_SALT = "salt";

    public static final String COL_PASSWORD = "password";

    public static final String COL_IMGURL = "imgurl";

    //注册参数
    public User(String loginname, String password) {
        this.loginname = loginname;
        this.password = password;
    }

    public User(String loginname, String username, String realname, Integer age, Integer sex, String address, String phone, String email, String password) {
        this.loginname = loginname;
        this.username = username;
        this.realname = realname;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}