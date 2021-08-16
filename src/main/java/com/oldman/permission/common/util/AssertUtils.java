package com.oldman.permission.common.util;


import com.oldman.permission.common.advice.ParamsException;

/**
 * 自定义断言工具类
 *
 * @author oldman
 * @date 2021/8/7 15:15
 */
public class AssertUtils {
    public static void isTrue(Boolean flag, String msg, Integer code) {
        if (flag) {
            throw new ParamsException(msg, code);
        }
    }
}
