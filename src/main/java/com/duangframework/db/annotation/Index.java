package com.duangframework.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

    // 索引名称
    String name() default "";

    // 索引类型 text, 2d, 2dsphere, hashed
    String type() default "text";

    // 索引方向 asc(升序)，desc(降序), both(两者)
    String order() default "asc";

    // 是否创建唯一索引
    boolean unique() default false;

}