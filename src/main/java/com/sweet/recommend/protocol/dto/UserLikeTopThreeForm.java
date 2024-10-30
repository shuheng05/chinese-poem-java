package com.sweet.recommend.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户喜欢的古诗中出现次数最多的前3个古诗形式
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeTopThreeForm {
    /**
     * 古诗形式
     */
    private String poemForm;

    /**
     * 出现次数
     */
    private Integer formCount;

}
