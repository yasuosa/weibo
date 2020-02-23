package com.ljr.weibo;

import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class WeiboApplicationTests {


    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;



    List<String> imgs=new ArrayList<>();
    @Test
    void contextLoads() {
        imgs.add("2020-02-13/A9460E22EF8841A4AD5420ED9E04569A.jpg_temp");
        imgs.add("2020-02-13/FE10111A96D0412B88B463FF163B4FAF.jpg_temp");
        newsService.saveImgByNid(1314231014,imgs);
    }

}
