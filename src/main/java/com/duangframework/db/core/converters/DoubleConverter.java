package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Double类型转换器
 *
 * @author Laotang
 */
public class DoubleConverter extends TypeConverter {

    public DoubleConverter() {
        super(double.class, Double.class, Double[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isFloat(type) || DataType.isFloatObject(type)) {
            return new Converter(field, getName(field), (Double)value);
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

        if(DataType.isFloat(type) || DataType.isFloatObject(type)) {
            return new Converter(field, getName(field), Double.parseDouble(String.valueOf(value)));
        } else {
            return converterEncodeDataSetType(field, value);
        }

    }


}
