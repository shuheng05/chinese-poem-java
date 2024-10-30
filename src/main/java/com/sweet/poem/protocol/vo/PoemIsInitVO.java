package com.sweet.poem.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户对古诗是否点赞、踩、收藏初始化
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemIsInitVO {


    /**
     * 是否点赞
     */
    private Integer isLiked;


    /**
     * 是否踩
     */
    private Integer isDisliked;


    /**
     * 是否点赞
     */
    private Integer isFavorite;
}
