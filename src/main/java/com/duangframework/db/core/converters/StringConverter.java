package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.mongodb.DBObject;

import java.lang.reflect.Field;
import java.util.List;


/**
 * 字符串类型转换器
 *
 * @author Laotang
 */
public class StringConverter extends TypeConverter {

    public StringConverter() {
        super(String.class, String[].class);
    }

    @Override
    public Object decode(Object object, DBObject dbObject) throws DbException {
        return null;
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        if(value instanceof List) {
            return new Converter(field, getName(field), (List)value);
        }

        return new Converter(field, getName(field), value);
    }


}
