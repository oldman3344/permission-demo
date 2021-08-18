package com.oldman.permission.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明需要绑定的实体注解
 * @author oldman
 * @date 2021/8/18 15:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindEntity {
    Class<?> value();
}
