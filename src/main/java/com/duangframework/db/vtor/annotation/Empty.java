package com.duangframework.db.vtor.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 允许为空验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Empty {

    boolean value() default  true;

    String message() default "";

}
