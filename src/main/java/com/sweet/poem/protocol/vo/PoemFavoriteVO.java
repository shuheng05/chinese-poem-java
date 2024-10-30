package com.sweet.poem.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏返回对象
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemFavoriteVO {
    /**
     * 点赞数
     */
    private Integer favoriteCount;

    /**
     * 是否点赞
     */
    private Integer isFavorite;

}
