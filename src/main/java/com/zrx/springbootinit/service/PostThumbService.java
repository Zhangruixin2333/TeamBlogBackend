package com.zrx.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrx.springbootinit.model.entity.PostThumb;
import com.zrx.springbootinit.model.entity.User;

import java.util.List;

/**
 * 帖子点赞服务
 *
 * 
 *
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @deprecated 使用数据实现的点赞已被弃用，请使用基于redis实现的点赞服务
     * @param postId postId
     * @param loginUser loginUser
     * @return return
     */
    @Deprecated
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId userId
     * @param postId postId
     * @return return
     */
    int doPostThumbInner(long userId, long postId);

    /**
     * 使用redis实现点赞
     * @param postId 博文id
     * @param userId 用户id
     * @return 指定博文点赞总数
     */
    int doPostThumbByRedis(long postId, long userId);
    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transThumbFromRedisToDB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transThumbCountFromRedisToDB();

    /**
     * 批量删除取消点赞数据
     * @param postNotThumb
     */
    void removeBatchCelThumb(List<PostThumb> postNotThumb);
}
