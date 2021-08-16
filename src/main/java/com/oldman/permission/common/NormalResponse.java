package com.oldman.permission.common;

import lombok.Getter;

/**
 * 自定义统一返回对象
 * @author oldman
 */
@Getter
public class NormalResponse<T> extends BaseResponse{

    private static final long serialVersionUID = 8036410145772382323L;

    private T result;
    //private int count;

    public NormalResponse(){
        super();
    }

    public NormalResponse(T result) {
        super();
        this.setResult(result);
    }

    public NormalResponse(Integer code) {
        super(code);
    }

    public NormalResponse(Integer code, String msg) {
        super(code, msg);
    }

    public NormalResponse(String msg) {
        super(msg);
    }

    public NormalResponse setResult(T result) {
        this.result = result;
        return this;
    }

    /*public NormalResponse setCount(Integer count) {
        this.count = count;
        return this;
    }*/
}
