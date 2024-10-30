package com.sweet.recommend.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户喜欢的古诗中出现次数最多的前3个古诗类型
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeTopThreeType {
    /**
     * 古诗类型
     */
    private String poemType;

    /**
     * 出现次数
     */
    private Integer typeCount;
}
