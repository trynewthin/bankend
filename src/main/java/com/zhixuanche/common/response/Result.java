package com.zhixuanche.common.response;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 
 * 注意：系统中同时存在 ApiResponse 和 Result 两种响应格式。
 * - ApiResponse 主要用于用户模块和全局异常处理
 * - Result 主要用于车辆模块
 * 
 * 两者功能类似，但实现细节有所不同。本类提供了与 ApiResponse 的兼容方法。
 */
public class Result implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 构造函数
     */
    private Result() {}

    /**
     * 构造函数
     * @param code 状态码
     * @param message 响应消息
     * @param data 响应数据
     */
    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     * @return Result对象
     */
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    /**
     * 成功响应（有数据）
     * @param data 响应数据
     * @return Result对象
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息）
     * @param message 响应消息
     * @param data 响应数据
     * @return Result对象
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    /**
     * 错误响应
     * @param code 错误状态码
     * @param message 错误消息
     * @return Result对象
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message, null);
    }

    /**
     * 错误响应（带数据）
     * @param code 错误状态码
     * @param message 错误消息
     * @param data 错误数据
     * @return Result对象
     */
    public static Result error(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }

    /**
     * 参数错误响应
     * @param message 错误消息
     * @return Result对象
     */
    public static Result badRequest(String message) {
        return new Result(400, message, null);
    }

    /**
     * 未授权响应
     * @return Result对象
     */
    public static Result unauthorized() {
        return new Result(401, "未授权访问", null);
    }

    /**
     * 禁止访问响应
     * @return Result对象
     */
    public static Result forbidden() {
        return new Result(403, "禁止访问", null);
    }

    /**
     * 资源不存在响应
     * @return Result对象
     */
    public static Result notFound() {
        return new Result(404, "资源不存在", null);
    }

    /**
     * 服务器错误响应
     * @return Result对象
     */
    public static Result serverError() {
        return new Result(500, "服务器内部错误", null);
    }

    /**
     * 将 Result 转换为 ApiResponse
     * @param <T> 数据类型
     * @return ApiResponse 对象
     */
    @SuppressWarnings("unchecked")
    public <T> ApiResponse<T> toApiResponse() {
        return ApiResponse.success(this.message, (T) this.data);
    }
    
    /**
     * 从 ApiResponse 创建 Result
     * @param apiResponse ApiResponse 对象
     * @return Result 对象
     */
    public static Result fromApiResponse(ApiResponse<?> apiResponse) {
        return new Result(apiResponse.getCode(), apiResponse.getMessage(), apiResponse.getData());
    }

    // Getter 和 Setter 方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
} 