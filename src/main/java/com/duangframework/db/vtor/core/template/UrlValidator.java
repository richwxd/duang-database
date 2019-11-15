package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.PatternKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.URL;

import java.lang.annotation.Annotation;

/**
 * URL验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class UrlValidator extends AbstractValidatorTemplate<URL> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return URL.class;
    }

    @Override
    public void handle(URL annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        if(ToolsKit.isNotEmpty(paramValue)) {
            boolean isUrl = false;
            if ("*".equals(annonation.regexp())) {
                isUrl = PatternKit.isURL(paramValue + "");
            } else {
                isUrl = PatternKit.isMatch(annonation.regexp(), paramValue + "");
            }
            if (!isUrl) {
                throw new ValidatorException(paramName + "[" + paramValue + "]" + annonation.message());
            }
        }
    }
}
