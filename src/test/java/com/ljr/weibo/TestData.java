package com.ljr.weibo;

import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther 任鹏宇
 * @Date 2020/2/29
 */
@SpringBootTest
public class TestData {

    @Autowired
    private UserService userService;

    @Test
    void initUserData() throws InterruptedException {
//        List<UserVo> userVos=new ArrayList<>();
//        UserVo admin=new UserVo("admin","超级管理员","超级管理员",21,1,"天上","110","110@qq.com","123456");
//        UserVo rpy=new UserVo("rpy","任鹏宇","任鹏宇",21,1,"天上","18255990660","601529188@qq.com","123456");
//        UserVo lqw=new UserVo("lqw","吕权威","吕权威",21,1,"天上","110","110@qq.com","123456");
//        UserVo zs=new UserVo("zs","张三","张三",21,1,"天上","110","110@qq.com","123456");
//        UserVo ls=new UserVo("ls","李四","李四",21,1,"天上","110","110@qq.com","123456");
//        UserVo ww=new UserVo("ww","王五","王五",21,1,"天上","110","110@qq.com","123456");
//        UserVo zmx=new UserVo("zmx","周梦想","周梦想",21,1,"天上","110","110@qq.com","123456");
//       userVos.add(admin);
//       userVos.add(rpy);
//       userVos.add(lqw);
//       userVos.add(zs);
//       userVos.add(ls);
//       userVos.add(ww);
//       userVos.add(zmx);
//        for (UserVo userVo : userVos) {
//            Thread.sleep(500);
//            userService.saveUser(userVo);
//        }

    }
}
