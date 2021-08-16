package com.oldman.permission.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 自定义统一返回对象
 * @author oldman
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -1286755086622143548L;

    private Integer code = Code.SUCCESS;

    private String msg;

    public BaseResponse(String msg) {
        this.msg = msg;
    }

    public BaseResponse(Integer code) {
        this.code = code;
    }
}
