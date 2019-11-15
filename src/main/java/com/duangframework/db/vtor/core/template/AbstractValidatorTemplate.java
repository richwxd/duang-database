package com.duangframework.db.vtor.core.template;

import com.duangframework.db.vtor.common.ValidatorException;

import java.lang.annotation.Annotation;

/**
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public abstract class AbstractValidatorTemplate<T> {

    public AbstractValidatorTemplate() {}

    public abstract Class<? extends Annotation> annotationClass();

    /**
     *
     * @param parameterType
     * @param paramName
     * @param paramValue
     * @return
     * @throws ValidatorException
     */
    protected abstract void handle(T annotation, Class<?> parameterType,  String paramName, Object paramValue) throws ValidatorException;

    public void vaildator(T annotation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        handle(annotation, parameterType, paramName, paramValue);
    }

}
