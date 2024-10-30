package com.sweet.common.unifyReturnObject;

import lombok.Data;

/**
 * 统一返回对象
 * @author shuheng
 */
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private  Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Result() {}
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success(String msg, T data){
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> error(String msg){
        return new Result<>(500, msg);
    }
}
