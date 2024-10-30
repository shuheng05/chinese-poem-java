package com.sweet.poem.protocol.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * user_dislike_poem表
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDislikePoemPO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 诗词id
     */
    private Integer poemId;
}