package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Character 类型转换器
 *
 * @author Laotang
 */
public class CharConverter extends TypeConverter {

    public CharConverter() {
        super(char.class, Character.class, char[].class, Character[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (null == value) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isChar(type) || DataType.isCharObject(type)) {
            return new Converter(field, getName(field), (Character)value);
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

        if(DataType.isChar(type) || DataType.isCharObject(type)) {
            return new Converter(field, getName(field),Character.valueOf((char)value));
        } else {
            return  converterEncodeDataSetType(field, value);
        }

    }


}
