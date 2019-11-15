package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.PatternKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.Email;

import java.lang.annotation.Annotation;

/**
 * 邮箱验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class EmailValidator extends AbstractValidatorTemplate<Email> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Email.class;
    }

    @Override
    public void handle(Email annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        if(ToolsKit.isNotEmpty(paramValue)) {
            boolean isEmail = false;
            if (!".*".equals(annonation.regexp())) {
                isEmail = PatternKit.isMatch(annonation.regexp(), paramValue.toString());
            } else {
                isEmail = PatternKit.isEmail(paramValue.toString());
            }
            if (!isEmail) {
                throw new ValidatorException(paramName + "[" + paramValue + "]" + annonation.message());
            }
        }
    }
}
