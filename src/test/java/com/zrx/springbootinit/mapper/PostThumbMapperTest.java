package com.zrx.springbootinit.mapper;

import com.zrx.springbootinit.model.entity.PostThumb;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zrx
 * @date 2023/9/4 16:05
 */
@SpringBootTest
public class PostThumbMapperTest {
    @Resource
    private PostThumbMapper postThumbMapper;

    @Test
    void listPostWithDelete() {
        List<PostThumb> postThumbs = new ArrayList<>();
        PostThumb postThumb1 = new PostThumb(570L, 873L);
        PostThumb postThumb2 = new PostThumb(944L, 24L);
        postThumbs.add(postThumb1);
        postThumbs.add(postThumb2);
        Integer integer = postThumbMapper.removeBatchCelThumb(postThumbs);
        System.out.println("受影响的行数：" + integer);
    }
}
