package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.PatternKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.Phone;

import java.lang.annotation.Annotation;

/**
 * 表达式验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class PhoneValidator extends AbstractValidatorTemplate<Phone> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Phone.class;
    }

    @Override
    public void handle(Phone annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        if(ToolsKit.isNotEmpty(paramValue)) {
            boolean isPhone =  false;
            if (!".*".equals(annonation.regexp())) {
                isPhone = PatternKit.isMatch(annonation.regexp(), paramValue.toString());
            } else {
                isPhone = PatternKit.isMobile(paramValue.toString());
            }

            if (!isPhone) {
                throw new ValidatorException(paramName + "[" + paramValue + "]" + annonation.message());
            }
        }
    }
}
