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

//        Class<?> type = field.getType();
//
//        Object toFieldValueObj = null;
//        if (DataType.isDouble(type) || DataType.isDoubleObject(type)) {
//            toFieldValueObj = convertValueObj(field, valueObj);
//        }

//        setFieldValue(entityObj, field, Double.parseDouble(String.valueOf(toFieldValueObj)));

        return new Converter(field, getName(field), (Double)value);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        if(value instanceof List) {
            return new Converter(field, getName(field), (List)value);
        }

        return new Converter(field, getName(field), Double.parseDouble(String.valueOf(value)));
    }


}
