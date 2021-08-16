package com.oldman.permission.common;

/**
 * 自定义统一返回对象状态码
 * @author oldman
 */
public class Code {
    /**
     * token无效
     */
    public static final int TOKEN_INVALID = 422;

    /**
     * 请求不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * 服务器错误
     */
    public static final int ERROR = 500;

    /**
     * 参数错误: 一般是缺少或参数值不符合要求
     */
    public static final int ARGUMENT_ERROR = -2;

    /**
     * 请求成功
     */
    public static final int SUCCESS = 200;


    /**
     * 请求失败
     */
    public static final int FAIL = -1;
}
