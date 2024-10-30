package com.sweet.poem.protocol.po;

import lombok.Data;

/**
 * 古诗词
 * @author shuheng
 */
@Data
public class Poem {
    /**
     * 古诗词id
     */
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
     * 古诗词作者id
     */
    private Integer authorId;

    /**
     * 古诗词朝代id
     */
    private Integer dynastyId;

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
}
