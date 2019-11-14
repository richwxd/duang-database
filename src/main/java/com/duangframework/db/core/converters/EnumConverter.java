package com.duangframework.db.core.converters;


import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;

import java.lang.reflect.Field;

/**
 * Enum 转换器
 *
 * @author Laotang
 */
public class EnumConverter extends TypeConverter {

    public EnumConverter() {
        super(Enum.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws Exception {
        if (value == null) {
            return null;
        }

        Class type = field.getType();

        return new Converter(field, getName(field), Enum.valueOf(type, String.valueOf(value)));

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if (value == null) {
            return null;
        }

        if(isSupported(field.getType())) {
            return new Converter(field, getName(field), getEnumName((Enum) value));
        }

        return null;

    }

    private boolean isSupported(final Class c) {
        return c.isEnum();
    }

    private <T extends Enum> String getEnumName(final T value) {
        return value.name();
    }
}
