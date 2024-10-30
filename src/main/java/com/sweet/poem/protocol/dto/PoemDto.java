package com.sweet.poem.protocol.dto;

import lombok.Data;

import java.util.List;


/**
 * 古诗词 vo
 * @author shuheng
 */
@Data
//@Document(indexName = "poem")
public class PoemDto {
    /**
     * 古诗词id
     */
    //@Id
    //@Field(type = FieldType.Integer, index = false)
    private Integer id;

    /**
     * 古诗词标题
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    /**
     * 古诗词内容
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    /**
     * 古诗词作者
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String author;

    /**
     * 古诗词朝代
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String dynasty;

    /**
     * 古诗词注释
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String annotation;

    /**
     * 古诗词译文
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String translation;

    /**
     * 古诗词赏析
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String appreciate;

    /**
     * 古诗词mp4地址
     */
    //@Field(type = FieldType.Keyword)
    private String mp4Url;

    /**
     * 古诗词形式
     */
    //@Field(type = FieldType.Keyword)
    private String form;

    /**
     * 古诗词类型
     */
    //@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String type;

    /**
     * 自动补全
     */
    private List<String> suggestion;
}
