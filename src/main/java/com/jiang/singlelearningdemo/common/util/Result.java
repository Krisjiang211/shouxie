package com.jiang.singlelearningdemo.common.util;

import lombok.Data;

@Data
public class Result<T> {
    private Boolean success;

    private Long timestamp;

    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
}
