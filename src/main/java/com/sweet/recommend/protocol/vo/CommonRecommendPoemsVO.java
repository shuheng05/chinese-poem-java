package com.sweet.recommend.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共推荐 10首
 * score = max(likeCount - dislikeCount, 0) * 0.4 + favoriteCount * 0.6
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonRecommendPoemsVO {

    /**
     * 诗词标题
     */
    private String poemTitle;

    /**
     * 诗词作者
     */
    private String poemAuthor;
}
