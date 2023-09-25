package com.zrx.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrx.springbootinit.common.ErrorCode;
import com.zrx.springbootinit.exception.BusinessException;
import com.zrx.springbootinit.mapper.PostThumbMapper;
import com.zrx.springbootinit.model.entity.Post;
import com.zrx.springbootinit.model.entity.PostThumb;
import com.zrx.springbootinit.model.entity.User;
import com.zrx.springbootinit.service.PostService;
import com.zrx.springbootinit.service.PostThumbService;
import com.zrx.springbootinit.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子点赞服务实现
 */
@Service
@Slf4j
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
        implements PostThumbService {

    @Resource
    private PostService postService;
    @Resource
    private RedisService redisService;

    // region 已经弃用的代码

    /**
     * 点赞
     *
     * @param postId    postId
     * @param loginUser loginUser
     * @return return
     */
    @Override
    @Deprecated
    public int doPostThumb(long postId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        PostThumbService postThumbService = (PostThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postThumbService.doPostThumbInner(userId, postId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId userId
     * @param postId postId
     * @return return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doPostThumbInner(long userId, long postId) {
        PostThumb postThumb = new PostThumb(userId, postId);

        QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>(postThumb);
        PostThumb oldPostThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldPostThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = postService.update()
                        .eq("id", postId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(postThumb);
            if (result) {
                // 点赞数 + 1
                result = postService.update()
                        .eq("id", postId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
    // endregion

    @Override
    public int doPostThumbByRedis(long postId, long userId) {
        if (redisService.checkIsThumb(postId, userId)) {
            redisService.unThumbFromRedis(postId, userId);
            redisService.decreasePostThumbCount(postId);
        } else {
            redisService.saveThumbToRedis(postId, userId);
            redisService.increasePostThumbCount(postId);
        }
        return redisService.getPostThumbCount(postId);
    }

    @Override
    public void transThumbFromRedisToDB() {

    }

    @Override
    public void transThumbCountFromRedisToDB() {

    }

    @Override
    @Transactional
    public void removeBatchCelThumb(List<PostThumb> postNotThumb) {
        if (CollectionUtils.isEmpty(postNotThumb)) {
            log.info("批量删除取消点赞数据，PostThumb 列表为空");
            return;
        }
        Integer hasNotThumbNum = this.baseMapper.removeBatchCelThumb(postNotThumb);
        log.info("删除redis中存储的取消点赞的数据，共{}条", hasNotThumbNum);
    }
}




