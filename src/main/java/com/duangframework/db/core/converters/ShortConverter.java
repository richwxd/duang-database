package com.duangframework.db.core.converters;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;


/**
 * Short 转换器
 *
 * @author Laotang
 */
public class ShortConverter extends TypeConverter {

    public ShortConverter() {
        super(short.class, Short.class, short[].class, Short[].class);
    }

    private Object convertToShortArray(Class type, List<?> values) {
        Object array = Array.newInstance(type, values.size());
        for (int i = 0; i < values.size(); i++) {
            Array.set(array, i, ((Number) values.get(i)).shortValue());
        }
        return array;
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }

        if (value instanceof Short) {
            return new Converter(field, getName(field), (Short)value);
        }

        if (value instanceof Number) {
            return new Converter(field, getName(field), ((Number) value).shortValue());
        }

        Class fieldType = field.getType();

        if(DataType.isShort(fieldType) || DataType.isShortObject(fieldType)) {
            return new Converter(field, getName(field), Short.parseShort(value.toString()));
        } else {
            final Class<?> type = fieldType.isArray() ? fieldType.getComponentType() : fieldType;
            return new Converter(field, getName(field), convertToShortArray(type, (List<?>) value));
        }

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Class<?> fieldType = field.getType();

        if(DataType.isShort(fieldType) || DataType.isShortObject(fieldType)) {
            return new Converter(field, getName(field), (short)value);
        } else {
            return  converterEncodeDataSetType(field, value);
        }
    }

}
