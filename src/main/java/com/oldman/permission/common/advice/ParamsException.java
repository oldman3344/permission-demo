package com.oldman.permission.common.advice;

import com.oldman.permission.common.Code;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义参数异常
 * @author oldman
 * @date 2021/8/7 15:21
 */
@Getter
@Setter
public class ParamsException extends RuntimeException{
    private String msg;
    private Integer code;

    public ParamsException(String msg,Integer code){
        this.msg=msg;
        this.code= code;
    }
}
