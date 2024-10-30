package com.sweet.user.protocol.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询用户收藏的诗词
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoritePoemsQuery {
    /**
     * 分页查询
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer limit;
}
