package com.zrx.springbootinit.model.dto.postthumb;

import lombok.Data;
import lombok.NonNull;

/**
 * 博文点赞总数 数据传输类
 * @author leev
 * @since 2023/8/15 8:52
 */
@Data
public class PostThumbCountDTO {
    /**
     * 博文id
     */
    @NonNull
    Long postId;
    /**
     * 点赞总数
     */
    @NonNull
    Integer thumbCount;
}
