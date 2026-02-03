package com.example.defectservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局统一返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    // 响应码：200成功，500失败
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败响应
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }
}