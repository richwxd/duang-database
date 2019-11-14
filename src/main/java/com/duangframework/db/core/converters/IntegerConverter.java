package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;


/**
 * Integer类型转换器
 *
 * @author Laotang
 */
public class IntegerConverter extends TypeConverter {

    public IntegerConverter() {
        super(int.class, Integer.class, int[].class, Integer[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isInteger(type) || DataType.isIntegerObject(type)) {
            return new Converter(field, getName(field), (Integer)value);
        } else {
            return converterDecodeDataType(field, value);
        }

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isInteger(type) || DataType.isIntegerObject(type)) {
            return new Converter(field, getName(field), Integer.parseInt(String.valueOf(value)));
        } else {
            return  converterEncodeDataSetType(field, value);
        }

    }
}
