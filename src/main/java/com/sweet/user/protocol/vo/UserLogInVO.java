package com.sweet.user.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInVO {
    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;
    /**
     * jwt token
     */
    private String token;
}
