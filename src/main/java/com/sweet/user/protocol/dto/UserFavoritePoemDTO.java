package com.sweet.user.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoritePoemDTO {
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
     * 诗词内容
     */
    private String poemContent;
}
