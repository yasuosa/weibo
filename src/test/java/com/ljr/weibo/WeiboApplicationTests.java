package com.ljr.weibo;

import com.ljr.weibo.domain.User;
import com.ljr.weibo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class WeiboApplicationTests {


    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        userService.save(new User(1,"admin",new Date(),"123"));
    }

}
