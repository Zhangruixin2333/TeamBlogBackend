package com.zrx.springbootinit.service;

import com.zrx.springbootinit.model.entity.PostThumb;

import java.util.List;

/**
 * @author leev
 * @since 2023/8/14 14:54
 */
public interface RedisService {
    /**
     *
     * @param thumbPostId 被点赞博文id
     * @param thumbUserId 发起点赞者id
     * @return 是否已经被点赞{true:已经被点赞,false:未被点赞}
     */
    boolean checkIsThumb(Long thumbPostId, Long thumbUserId);
    /**
     * 点赞
     *
     * @param thumbPostId 被点赞博文id
     * @param thumbUserId 发起点赞者id
     */
    void saveThumbToRedis(Long thumbPostId, Long thumbUserId);

    /**
     * 取消点赞
     *
     * @param thumbPostId 被点赞博文id
     * @param thumbUserId 发起点赞者id
     */
    void unThumbFromRedis(Long thumbPostId, Long thumbUserId);

    /**
     * 删除一条点赞数据
     *
     * @param thumbPostId 被点赞博文id
     * @param thumbUserId 发起点赞者id
     */
    void deleteThumbFromRedis(Long thumbPostId, Long thumbUserId);

    /**
     * 该博文点赞数加1
     *
     * @param thumbPostId 被点赞博文id
     */
    void increasePostThumbCount(Long thumbPostId);

    /**
     * 该博文点赞数减1
     *
     * @param thumbPostId 被点赞博文id
     */
    void decreasePostThumbCount(Long thumbPostId);

    /**
     *
     * @param thumbPostId 被点赞博文id
     * @return 指定博文点赞总数
     */
    Integer getPostThumbCount(Long thumbPostId);
    /**
     * @return 所有博文点赞数据
     */
    List<PostThumb> getAllPostThumbData();

    /**
     * @return 所有博文点赞数
     */
    List<?> getAllPostThumbCount();
}
