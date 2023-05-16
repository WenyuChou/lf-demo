package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouwenyu
 * date 2021-12-10
 */
@Data
public class ApiResult<T> implements Serializable {

    private static final String success = "200";
    private static final String fail = "500";

    private String code;
    private String message;
    private String msg;
    private T data;

    public ApiResult() {
    }

    public ApiResult(String code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setMsg(message);
        this.setData(data);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(ApiResult.success, message, data);
    }

    public static <T> ApiResult<T> success(T data) {
        return success(ApiResult.success, data);
    }

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> fail(String message, T data) {
        return new ApiResult<>(ApiResult.fail, message, data);

    }

    public static <T> ApiResult<T> fail(T data) {
        return fail(ApiResult.fail, data);
    }

    public static <T> ApiResult<T> fail(String message) {
        return fail(message, null);
    }

    public static <T> ApiResult<T> fail() {
        return fail(null);
    }

    public boolean checkFail() {
        return !this.getCode().equals(ApiResult.success);
    }

}
