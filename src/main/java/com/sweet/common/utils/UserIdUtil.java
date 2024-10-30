package com.sweet.common.utils;


/**
 * 基于TreadLocal的工具类,用于保存和获取当前登录用户id
 * @author shuheng
 */
public class UserIdUtil {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前用户id
     */
    public static void setCurrentUserId(String userId){
        threadLocal.set(userId);
    }

    /**
     * 获取当前用户id
     */
    public static String getCurrentUserId(){
        return threadLocal.get();
    }
}
