package com.duangframework.db.vtor.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 表达式验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pattern {

    String regexp() default ".*";

    String message() default "验证不通过";

    String defaultValue() default "";

    boolean isEmpty() default true;
}
