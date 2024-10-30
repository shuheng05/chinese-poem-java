package com.sweet.user.protocol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInVO {
    private String id;

    private String username;

    private String token;
}
