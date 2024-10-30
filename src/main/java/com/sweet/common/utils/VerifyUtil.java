package com.sweet.common.utils;


import java.util.regex.Pattern;

/**
 * 验证邮箱，密码等工具类
 * @author shuheng
 */
public class VerifyUtil {
    /**
     * 验证邮箱
     */
    public static boolean isEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        String reg = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        return Pattern.matches(reg, email);
    }

    /**
     * 密码必须大于8位且至少包含两种字符
     * 验证密码
     */
    public static boolean isPassword(String password) {
        if (password == null || "".equals(password)) {
            return false;
        }

        String reg = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{9,}|^(?=.*[a-zA-Z])(?=.*[\\W_])[a-zA-Z\\W_]{9,}|^(?=.*\\d)(?=.*[\\W_])\\d\\W_{9,}$";
        return Pattern.matches(reg, password);
    }
}
