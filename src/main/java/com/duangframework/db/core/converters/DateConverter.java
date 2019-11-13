package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;
import java.util.Date;


/**
 * Double类型转换器
 *
 * @author Laotang
 */
public class DateConverter extends TypeConverter {

    public DateConverter() {
        super(Date.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }
//
//        Class<?> type = field.getType();
//
//        Object toFieldValueObj = null;
//        if (DataType.isDate(type)) {
//            toFieldValueObj = convertValueObj(field, valueObj);
//        }
//        setFieldValue(entityObj, field, toFieldValueObj);

        return new Converter(field, getName(field), (Date)value);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        return new Converter(field, getName(field), value);
    }


}
