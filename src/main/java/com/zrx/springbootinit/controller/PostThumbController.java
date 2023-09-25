package com.zrx.springbootinit.controller;

import com.zrx.springbootinit.common.BaseResponse;
import com.zrx.springbootinit.common.ErrorCode;
import com.zrx.springbootinit.common.ResultUtils;
import com.zrx.springbootinit.exception.BusinessException;
import com.zrx.springbootinit.model.dto.postthumb.PostThumbAddRequest;
import com.zrx.springbootinit.model.entity.User;
import com.zrx.springbootinit.service.PostThumbService;
import com.zrx.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
@Api(tags = "帖子点赞接口")
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    // region 已经弃用的代码

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest postThumbAddRequest
     * @param request request
     * @return resultNum 本次点赞变化数
     * @deprecated 该方法已经禁用
     */
    @PostMapping("/")
    @Deprecated
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }
    //endregion

    @PostMapping("/doThumbByRedis")
    public BaseResponse<Integer> doThumbByRedis(@RequestBody PostThumbAddRequest postThumbAddRequest, HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        long userId = loginUser.getId();
        return ResultUtils.success(postThumbService.doPostThumbByRedis(postId, userId));
    }
}
