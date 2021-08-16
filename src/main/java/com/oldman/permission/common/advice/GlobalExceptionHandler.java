package com.oldman.permission.common.advice;

import com.oldman.permission.common.Code;
import com.oldman.permission.common.NormalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 全局异常处理器
 *
 * @author oldman
 * @date 2021/8/7 15:23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ParamsException.class)
    public NormalResponse paramsExceptionHandler(ParamsException e) {
        log.error("异常信息: {}",e.getMsg());
        return new NormalResponse(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public NormalResponse exceptionHandler(Exception e) {
        log.error("异常类型: {}，异常详情：{}，异常文件：{}，异常方法：{}，行号：{}",e.getClass().getName(),e.getMessage(),e.getStackTrace()[0].getFileName(),e.getStackTrace()[0].getMethodName(),e.getStackTrace()[0].getLineNumber());
        return new NormalResponse(Code.ERROR, "出现服务异常，请联系管理员");
    }



    @ExceptionHandler(BindException.class)
    public NormalResponse bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        log.error("异常信息: {}",e.getMessage());
        return new NormalResponse(Code.ARGUMENT_ERROR, collect.toString().substring(1, collect.toString().length() - 1));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public NormalResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        log.error("异常信息: {}",e.getMessage());
        return new NormalResponse(Code.ARGUMENT_ERROR, collect.toString().substring(1, collect.toString().length() - 1));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public NormalResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        log.error("异常信息: {}",e.getMessage());
        return new NormalResponse(Code.ARGUMENT_ERROR, collect.toString().substring(1, collect.toString().length() - 1));
    }
}
