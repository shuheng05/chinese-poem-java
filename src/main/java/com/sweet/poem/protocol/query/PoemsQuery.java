package com.sweet.poem.protocol.query;

import lombok.Data;

/**
 * 查询诗词参数
 * @author shuheng
 */
@Data
public class PoemsQuery {
    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer limit;

    /**
     * 搜索类型
     */
    private String type;

    /**
     * 作者
     */
    private String author;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 形式
     */
    private String form;

    /**
     * 搜索
     */
    private String content;
}
