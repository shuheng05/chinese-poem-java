package com.sweet.recommend.protocol.query;

import lombok.Data;

@Data
public class RecommendPageQuery {

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer limit;
}
