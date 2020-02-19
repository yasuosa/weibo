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

    //数量
    private Long total;

    private Object data;

    public DataGridView(Long total, Object data) {
        this.total = total;
        this.data = data;
    }

    public DataGridView(Object data) {
        this.code=Constant.CODE_TRUE;
        this.msg="";
        this.data = data;
    }
}
