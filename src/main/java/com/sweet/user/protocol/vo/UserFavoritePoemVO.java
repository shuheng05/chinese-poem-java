package com.sweet.user.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户收藏的诗词
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoritePoemVO {
    /**
     * 诗词id
     */
    private Integer poemId;

    /**
     * 诗词标题
     */
    private String poemTitle;

    /**
     * 诗词作者
     */
    private String poemAuthor;

    /**
     * 诗词首句
     */
    private String poemHeadContent;
}
