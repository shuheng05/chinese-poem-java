package com.sweet.recommend.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户喜欢的古诗中出现次数最多的前3个作者
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeTopThreeAuthor {
    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 出现次数
     */
    private Integer authorCount;
}
