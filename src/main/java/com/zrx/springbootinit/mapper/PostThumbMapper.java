package com.zrx.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrx.springbootinit.model.entity.PostThumb;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子点赞数据库操作
 *
 * 
 *
 */
public interface PostThumbMapper extends BaseMapper<PostThumb> {

    /**
     * 删除没有取消点赞的数据（Redis中的）
     * @param postNotThumb
     */
    Integer removeBatchCelThumb(@Param("list") List<PostThumb> postNotThumb);

}




