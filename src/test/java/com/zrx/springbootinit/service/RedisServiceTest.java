package com.zrx.springbootinit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author leev
 * @since 2023/8/14 16:20
 */
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    private RedisService redisService;
    @Test
    void saveThumbRedisToRedis(){
//        redisService.saveThumbToRedis("POST001","USER001");
    }

}
