package com.sweet.poem.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 古诗点赞、踩、收藏数初始化
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemCountInitVO {
    /**
     * 点赞数
     */
    private Integer likeCount;


    /**
     * 踩数
     */
    private Integer dislikeCount;


    /**
     * 点赞数
     */
    private Integer favoriteCount;

}
