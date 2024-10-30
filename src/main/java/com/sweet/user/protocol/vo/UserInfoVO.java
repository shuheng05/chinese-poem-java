package com.sweet.user.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户个人主页显示信息
 * @author shuheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    private String username;
    private String email;
    private String avatar;
    private String address;
}
