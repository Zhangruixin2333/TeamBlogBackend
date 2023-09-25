package com.zrx.springbootinit.model.enums;

import lombok.Getter;

/**
 * @author leev
 * @since 2023/8/14 16:00
 */
@Getter
public enum ThumbStatusEnum {
    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞");

    private final Integer code;

    private final String msg;

    ThumbStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
