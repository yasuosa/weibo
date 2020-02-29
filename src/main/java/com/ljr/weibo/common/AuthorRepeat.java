package com.ljr.weibo.common;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRepeat implements Serializable {

    private Integer index;
    private String name;
    private String content="//";
    private Integer userid;
    private String icon;

}
