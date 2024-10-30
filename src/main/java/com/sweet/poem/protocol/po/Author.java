package com.sweet.poem.protocol.po;

import lombok.Data;

/**
 * 诗词作者
 * @author shuheng
 */
@Data
public class Author {
    /**
     * 诗词作者id
     */
    private Integer id;

    /**
     * 诗词作者名称
     */
    private String name;

    /**
     * 诗词作者出生年份
     */
    private Integer birthYear;

    /**
     * 诗词作者死亡年份
     */
    private Integer deathYear;

    /**
     * 诗词作者朝代id
     */
    private Integer dynastyId;

    /**
     * 诗词作者简介
     */
    private String biography;
}
