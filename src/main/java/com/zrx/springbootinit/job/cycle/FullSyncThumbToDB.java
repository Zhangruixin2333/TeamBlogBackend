package com.zrx.springbootinit.job.cycle;

import cn.hutool.core.collection.CollUtil;
import com.zrx.springbootinit.constant.PostThumbConstant;
import com.zrx.springbootinit.model.entity.Post;
import com.zrx.springbootinit.model.entity.PostThumb;
import com.zrx.springbootinit.service.PostService;
import com.zrx.springbootinit.service.PostThumbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leev
 * @since 2023/8/15 11:07
 * @update  zrx
 * idea: 服务启动的时候，就将所有点赞数据加载到redis，取数据直接在redis中取值
 */
@Component
@Slf4j
public class FullSyncThumbToDB {
    @Resource
    private PostService postService;
    @Resource
    private PostThumbService postThumbService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每半分钟执行一次
     */
    @Scheduled(fixedRate = 30 * 1000)
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        // 取出Redis中的点赞数据
        Map<Object, Object> thumbCount = redisTemplate.opsForHash().entries(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT);
        // TODO 这里使用的keys，后续改为scan
        Set<String> thumbKeys = redisTemplate.keys(PostThumbConstant.MAP_KEY_POST_THUMB + "*");
        // 待删除的redis中的是否以点赞数据
        Map<String, List<Long>> willDelPostThumb = new HashMap<>();
        log.info("获取到的 MAP_KEY_POST_THUMB 数据：{}", thumbKeys);
        List<String> thumbPostIds;
        List<PostThumb> postIsThumb = new ArrayList<>();
        List<PostThumb> postNotThumb = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(thumbKeys)) {
            thumbPostIds = thumbKeys.stream().map(s -> {
                String[] parts = s.split(":");
                return parts[parts.length - 1];
            }).collect(Collectors.toList());
            // 博文id--用户id，删除DB未点赞的数据
            for (String thumbPostId : thumbPostIds) {
                // 用户id--是否已点赞
                Map<Object, Object> userIdAndIsThumb = redisTemplate.opsForHash()
                        .entries(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId);
                // 删除DB未点赞数据
                for (Map.Entry<Object, Object> next : userIdAndIsThumb.entrySet()) {
                    // TODO 强转为 Long，可能有问题
                    Long userId = (Long) next.getKey();
                    // 处理要在redis中删除的点赞数据
                    List<Long> userIdList = willDelPostThumb.getOrDefault(PostThumbConstant.MAP_KEY_POST_THUMB, new ArrayList<>());
                    userIdList.add(userId);
                    willDelPostThumb.put(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId, userIdList);
                    Integer isThumb = (Integer) next.getValue();
                    // 未点赞
                    if (isThumb == 0) {
                        PostThumb postThumb = new PostThumb(Long.valueOf(thumbPostId), userId);
                        postNotThumb.add(postThumb);
                    } else {
                        PostThumb postThumb = new PostThumb(Long.valueOf(thumbPostId), userId);
                        postIsThumb.add(postThumb);
                    }
                }
            }
            log.info("postThumb列表: {}", postIsThumb);
            // 把已点赞的数据新增，取消点赞的数据删除
            postThumbService.saveBatch(postIsThumb);
            postThumbService.removeBatchCelThumb(postNotThumb);
        }
        if (CollUtil.isNotEmpty(thumbCount)) {
            Set<Object> postSet = thumbCount.keySet();
            List<Post> posts = new ArrayList<>();
            for (Object postId : postSet) {
                Post post = new Post();
                post.setId((Long) postId);
                post.setThumbNum((Integer) thumbCount.get(postId));
                posts.add(post);
            }
            // 更新博文点赞量到DB
            postService.updateBatchById(posts);
            // 删除Redis中的点赞数据
            Long deleteCountNum = redisTemplate.opsForHash().delete(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT,
                    posts.stream().map(Post::getId).toArray());
            log.info("删除了{}条 Post:Thumb:POST_THUMB_COUNT 数据：", deleteCountNum);
        }
        if (CollUtil.isNotEmpty(willDelPostThumb)) {
            willDelPostThumb.forEach((key, userIdList) -> {
                Long delNum = redisTemplate.opsForHash().delete(key, userIdList.toArray());
                log.info("删除了 {} 的 {} 条数据：", key, delNum);
            });
        }
        log.info("同步点赞定时器执行完毕");
    }
}
