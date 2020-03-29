package com.ljr.weibo.common;

import com.ljr.weibo.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/3/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentTree {

    private Integer id;
    private Integer pid;
    private Integer userid;
    private String username;
    private String icon;
    private Date time;
    private String comment;
    private List<CommentTree> childs=new ArrayList<>();

    public CommentTree(Integer id, Integer pid, Integer userid, String username, String icon, Date time, String comment) {
        this.id = id;
        this.pid = pid;
        this.userid = userid;
        this.username = username;
        this.icon = icon;
        this.time = time;
        this.comment = comment;
    }

    public static class CommentTreeBuilder{
        public static List<CommentTree> build(List<CommentTree> data,Integer topid){
            List<CommentTree> comments=new ArrayList<>();
            for (CommentTree d1 : data) {
                if(topid.equals(d1.getPid())){
                    comments.add(d1);
                }
                for (CommentTree d2 : data) {
                    if(d1.getId().equals(d2.getPid())){
                        d1.getChilds().add(d2);
                    }
                }
            }
            return comments;
        }
    }
}


