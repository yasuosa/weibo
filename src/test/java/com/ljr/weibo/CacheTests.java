package com.ljr.weibo;

import com.ljr.weibo.domain.CacheData;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.service.UserService;
import com.ljr.weibo.utils.SysUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CacheTests {



    @Test
    void contextLoads() throws IOException {
        System.out.println(SysUtils.buildContent("123"));
    }

}
