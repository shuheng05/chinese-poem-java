package com.sweet.poem.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞返回对象
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemLikeVO {
    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否点赞
     */
    private Integer isLiked;

    /**
     * 踩数
     */
    private Integer dislikeCount;

    /**
     * 是否踩
     */
    private Integer isDisliked;
}
