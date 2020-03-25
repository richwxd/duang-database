package com.duangframework.db.vtor.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 最小值验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Min {

    double value() default 0;

    String message() default "不能小于[${value}]!";

    boolean isEmpty() default true;

}
