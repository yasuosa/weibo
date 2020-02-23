package com.ljr.weibo;

import com.ljr.weibo.domain.CacheData;
import com.ljr.weibo.service.NewsService;
import com.ljr.weibo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CacheTests {


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    void contextLoads() {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
//        opsForValue.set("age","111");
        opsForValue.set("cache:1",new CacheData(1,"ssss"));
    }

}
