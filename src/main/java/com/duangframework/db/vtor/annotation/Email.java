package com.duangframework.db.vtor.annotation;

import com.duangframework.db.vtor.common.PatternKit;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 邮箱地址验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Email {

    String regexp() default PatternKit.EMAIL_REGEX; //".*";

    String message() default "不是一个正确的Email地址";

    String defaultValue() default "";

    boolean isEmpty() default true;

}
