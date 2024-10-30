package com.sweet.recommend.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户个性推荐
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendPoemsVO {
    /**
     * 古诗词id
     */
    //@Id
    private Integer id;

    /**
     * 古诗词标题
     */
    private String title;

    /**
     * 古诗词内容
     */
    private String content;

    /**
     * 古诗词作者
     */
    private String author;

    /**
     * 古诗词朝代
     */
    private String dynasty;

    /**
     * 古诗词注释
     */
    private String annotation;

    /**
     * 古诗词译文
     */
    private String translation;

    /**
     * 古诗词赏析
     */
    private String appreciate;

    /**
     * 古诗词mp4地址
     */
    private String mp4Url;

    /**
     * 古诗词形式
     */
    private String form;

    /**
     * 古诗词类型
     */
    private String type;

    /**
     * 自动补全
     */
    private List<String> suggestion;
}
