package com.ljr.weibo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回json的统一格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridView {

    private Integer code;
    private String msg;
    private Object data;

    public DataGridView(Object data) {
        this.code=Constant.CODE_TRUE;
        this.msg="";
        this.data = data;
    }
}
