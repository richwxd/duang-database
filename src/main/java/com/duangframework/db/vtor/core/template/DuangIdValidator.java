package com.duangframework.db.vtor.core.template;

import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.DuangId;

import java.lang.annotation.Annotation;

/**
 * DuangId从验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class DuangIdValidator extends AbstractValidatorTemplate<DuangId> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return DuangId.class;
    }

    @Override
    public void handle(DuangId annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy && ToolsKit.isEmpty(paramValue)) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }

        if(ToolsKit.isNotEmpty(paramValue)) {
            if (!ToolsKit.isValidDuangId(paramValue.toString())) {
                throw new ValidatorException(paramName + "[" + paramValue + "]" + annonation.message());
            }
        }
    }
}
