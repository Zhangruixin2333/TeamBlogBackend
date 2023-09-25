package com.zrx.springbootinit;

import com.zrx.springbootinit.config.WxOpenConfig;
import com.zrx.springbootinit.constant.PostThumbConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 主类测试
 *
 * 
 *
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

    @Test
    void testRedisKeys() {
//        Map<Object, Object> entries =
//                redisTemplate.opsForHash().entries("Post:Thumb:POST_THUMB_COUNT");
//        Object[] postIds = entries.keySet().toArray();
//        String postId = String.valueOf(postIds[0]);
//        Object o = redisTemplate.opsForHash().delete("Post:Thumb:POST_THUMB_COUNT", 1648547018689978374L);
//        System.out.println(o);
        Set<String> keys = redisTemplate.keys(PostThumbConstant.MAP_KEY_POST_THUMB + "*");
        System.out.println(keys);
    }

    @Test
    void testDelRedisDataByKeys() {
        // TODO 测试long类型  对于  redis中的数据删除效果---为解决定时任务无法删除数据的问题
        List<Long> ids = new ArrayList<>();
        ids.add(1648547018689978373L);
        ids.add(1648547018689978374L);
        redisTemplate.opsForHash().delete(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT, ids.toArray());
    }

}
