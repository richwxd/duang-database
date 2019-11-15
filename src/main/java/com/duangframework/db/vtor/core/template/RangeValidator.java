package com.duangframework.db.vtor.core.template;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.annotation.Range;

import java.lang.annotation.Annotation;

/**
 * 取值范围验证处理器
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
public class RangeValidator extends AbstractValidatorTemplate<Range> {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return Range.class;
    }

    @Override
    public void handle(Range annonation, Class<?> parameterType, String paramName, Object paramValue) throws ValidatorException {

        boolean isEmapy = annonation.isEmpty();
        if(isEmapy) {
            return;
        }
        if(ToolsKit.isEmpty(paramValue)) {
            throw new ValidatorException(paramName + "不能为空");
        }
        if(ToolsKit.isNotEmpty(parameterType)) {
            Double value =Double.parseDouble(paramValue.toString());
            double max = annonation.max();
            double min = annonation.min();
            double[] annonValues = annonation.value();
            if(annonValues[0] !=-1d && annonValues[1] != -1d) {
                min = annonValues[0];
                max = annonValues[1];
            }
            if( value > max || value< min ) {
                String maxString = max+"";
                String minString = min+"";
                if(DataType.isInteger(parameterType) || DataType.isIntegerObject(parameterType)) {
                    maxString = Double.valueOf(max).intValue()+"";
                    minString = Double.valueOf(min).intValue()+"";
                }
                String message = paramName+"["+paramValue+"]"+annonation.message().replace("${min}", minString).replace("${max}", maxString);
                throw new ValidatorException(message);
            }
        }
    }
}
