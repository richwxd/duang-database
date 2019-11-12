package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.List;


/**
 * String类型转换器
 *
 * @author Laotang
 */
public class StringConverter extends TypeConverter {

    public StringConverter() {
        super(String.class, String[].class);
    }

    @Override
    public void decode(Object entityObj, Field field, Object valueObj) throws DbException {

        if (null == valueObj) {
            return ;
        }

        Class<?> type = field.getType();

        Object toFieldValueObj = null;
        if (DataType.isString(type)) {
            toFieldValueObj = convertValueObj(field, valueObj);
        }

        setFieldValue(entityObj, field, String.valueOf(toFieldValueObj));
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        if(value instanceof List) {
            return new Converter(field, getName(field), (List)value);
        }

        return new Converter(field, getName(field), String.valueOf(value));
    }


}
