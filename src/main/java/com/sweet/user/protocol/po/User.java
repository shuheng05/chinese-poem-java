package com.sweet.user.protocol.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 主键id uuid
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;
}
