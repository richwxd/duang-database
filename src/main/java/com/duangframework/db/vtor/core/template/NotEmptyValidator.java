package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.NotEmpty;

import java.lang.annotation.Annotation;

/**
 * 不为空验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class NotEmptyValidator extends AbstractValidatorTemplate<NotEmpty> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return NotEmpty.class;
    }

    @Override
    public void handle(NotEmpty annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName+"["+paramValue+"]"+ annonation.message());
        }
    }
}
