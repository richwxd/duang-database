package com.duangframework.db.core.converters;


import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;
import java.lang.reflect.Field;


/**
 * Boolean
 *
 * @author Laotang
 */
public class BooleanConverter extends TypeConverter {

    public BooleanConverter() {
        super(boolean.class, Boolean.class, boolean[].class, Boolean[].class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return new Converter(field, getName(field), (Boolean)value);
        } if (value instanceof Number) {
            return  new Converter(field, getName(field),((Number) value).intValue() != 0);
        } else {
            return converterDecodeDataType(field, value);
        }

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        Class<?> type = field.getType();

        if(DataType.isBoolean(type) || DataType.isBooleanObject(type)) {
            return new Converter(field, getName(field), Boolean.parseBoolean(String.valueOf(value)));
        } else {
            return  converterEncodeDataSetType(field, value);
        }

    }

}
