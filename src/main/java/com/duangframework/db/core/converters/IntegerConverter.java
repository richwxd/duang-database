package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Integer类型转换器
 *
 * @author Laotang
 */
public class IntegerConverter extends TypeConverter {

    public IntegerConverter() {
        super(int.class, Integer.class, Integer[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

//        Class<?> type = field.getType();
//
//        Object toFieldValueObj = null;
//        if (DataType.isInteger(type) || DataType.isIntegerObject(type)) {
//            toFieldValueObj = convertValueObj(field, valueObj);
//        }

//        setFieldValue(entityObj, field, Integer.parseInt(String.valueOf(toFieldValueObj)));
        return new Converter(field, getName(field), (Integer)value);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        if(value instanceof List) {
            return new Converter(field, getName(field), (List)value);
        }

        return new Converter(field, getName(field), Integer.parseInt(String.valueOf(value)));
    }


}
