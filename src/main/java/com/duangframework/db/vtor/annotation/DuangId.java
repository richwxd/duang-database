package com.duangframework.db.vtor.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * 是否DuangId验证(与MongoDB ObjectId一致)
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DuangId {

    String message() default "不是DuangId";

    String defaultValue() default "";

    boolean isEmpty() default false;        // 默认为不允许为空

}
