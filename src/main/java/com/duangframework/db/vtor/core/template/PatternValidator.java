package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.PatternKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.Pattern;

import java.lang.annotation.Annotation;

/**
 * 表达式验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class PatternValidator extends AbstractValidatorTemplate<Pattern> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Pattern.class;
    }

    @Override
    public void handle(Pattern annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        if(ToolsKit.isNotEmpty(paramValue)) {
            boolean isPattern = false;
            if (!".*".equals(annonation.regexp())) {
                isPattern = PatternKit.isMatch(annonation.regexp(), paramValue.toString());
            } else {
                isPattern = true;
            }

            if (!isPattern) {
                throw new ValidatorException(paramName + "[" + paramValue + "]" + annonation.message());
            }
        }
    }
}
