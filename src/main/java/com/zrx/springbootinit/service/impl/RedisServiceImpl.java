package com.zrx.springbootinit.service.impl;

import com.zrx.springbootinit.constant.PostThumbConstant;
import com.zrx.springbootinit.model.dto.postthumb.PostThumbCountDTO;
import com.zrx.springbootinit.model.entity.PostThumb;
import com.zrx.springbootinit.model.enums.ThumbStatusEnum;
import com.zrx.springbootinit.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leev
 * @since 2023/8/14 15:15
 */
@Service
@Slf4j
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean checkIsThumb(Long thumbPostId, Long thumbUserId) {
//        String key = RedisUtils.getThumbKEY(thumbPostId, thumbUserId);
        Integer isCheck = (Integer) redisTemplate.opsForHash().get(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId, thumbUserId);
        if (isCheck == null || isCheck == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void saveThumbToRedis(Long thumbPostId, Long thumbUserId) {
//        String key = RedisUtils.getThumbKEY(thumbPostId, thumbUserId);
        redisTemplate.opsForHash().put(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId,
                thumbUserId, ThumbStatusEnum.LIKE.getCode());
    }

    @Override
    public void unThumbFromRedis(Long thumbPostId, Long thumbUserId) {
//        String key = RedisUtils.getThumbKEY(thumbPostId, thumbUserId);
        redisTemplate.opsForHash().put(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId,
                thumbUserId, ThumbStatusEnum.UNLIKE.getCode());
    }

    @Override
    public void deleteThumbFromRedis(Long thumbPostId, Long thumbUserId) {
//        String key = RedisUtils.getThumbKEY(thumbPostId, thumbUserId);
        redisTemplate.opsForHash().delete(PostThumbConstant.MAP_KEY_POST_THUMB + thumbPostId, thumbUserId);
    }

    @Override
    public void increasePostThumbCount(Long thumbPostId) {
        redisTemplate.opsForHash().increment(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT, thumbPostId, 1);
    }

    @Override
    public void decreasePostThumbCount(Long thumbPostId) {
        redisTemplate.opsForHash().increment(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT, thumbPostId, -1);
    }

    @Override
    public Integer getPostThumbCount(Long thumbPostId) {
        return (Integer) redisTemplate.opsForHash().get(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT, thumbPostId);
    }

    @Override
    public List<PostThumb> getAllPostThumbData() {
        List<PostThumb> list = new ArrayList<>();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(PostThumbConstant.MAP_KEY_POST_THUMB, ScanOptions.NONE)) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                // 点赞状态不为1，不存
                if ((Integer) entry.getValue() != 1) {
                    continue;
                }
                String key = (String) entry.getKey();

                String[] split = key.split("::");
                Long thumbPostId = Long.valueOf(split[0]);
                Long thumbUserId = Long.valueOf(split[1]);

                //组装成 PostThumb 对象
                PostThumb postThumb = new PostThumb(thumbPostId, thumbUserId);
                list.add(postThumb);
            }
        } catch (Exception e) {
            log.error("Redis错误--从redis中取得点赞记录错误");
        }

        return list;
    }

    @Override
    public List<PostThumbCountDTO> getAllPostThumbCount() {
        List<PostThumbCountDTO> list = new ArrayList<>();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(PostThumbConstant.MAP_KEY_POST_THUMB_COUNT, ScanOptions.NONE)) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                //将点赞数量存储在 LikedCountDT
                Long postId = (Long) map.getKey();
                PostThumbCountDTO dto = new PostThumbCountDTO(postId, (Integer) map.getValue());
                list.add(dto);
            }
        } catch (Exception e) {
            log.error("Redis错误--从redis中取得帖子点赞总数记录错误");
        }
        return list;
    }
}