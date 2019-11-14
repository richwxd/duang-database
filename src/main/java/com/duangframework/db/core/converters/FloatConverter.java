package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;


/**
 * Double类型转换器
 *
 * @author Laotang
 */
public class FloatConverter extends TypeConverter {

    public FloatConverter() {
        super(float.class, Float.class, Float[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isFloat(type) || DataType.isFloatObject(type)) {
            return new Converter(field, getName(field), (Float)value);
        } else {
            return converterDecodeDataType(field, value);
        }

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isFloat(type) || DataType.isFloatObject(type)) {
            return new Converter(field, getName(field), Float.parseFloat(String.valueOf(value)));
        } else {
            return converterEncodeDataSetType(field, value);
        }

    }

}
