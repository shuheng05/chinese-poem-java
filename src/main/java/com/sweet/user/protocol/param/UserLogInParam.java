package com.sweet.user.protocol.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录参数
 * @author shuheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogInParam {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;
}
