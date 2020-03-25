package com.duangframework.db.vtor.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 日期验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Ymd {

    String format() default "yyyy-MM-dd HH:mm:ss";

    String message() default "日期格式不正确[yyyy-MM-dd HH:mm:ss]";

    String defaultValue() default "yyyy-MM-dd HH:mm:ss";

    boolean isEmpty() default true;

}
