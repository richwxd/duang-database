package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * EnumSet
 *
 * @author Laotang
 */
public class EnumSetConverter extends TypeConverter {

    private final EnumConverter ec = new EnumConverter();

    public EnumSetConverter() {
        super(EnumSet.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws Exception {

        if (value == null) {
            return null;
        }

        Class enumType = field.getType();
        List enumList = (List) value;
        Object decodeObj = null;

        if (enumList.isEmpty()) {
            decodeObj = EnumSet.noneOf(enumType);
        } else {
            final List enums = new ArrayList();
            for (Object object : enumList) {
                enums.add(Enum.valueOf(enumType, String.valueOf(object)));
            }
            decodeObj = EnumSet.copyOf(enums);
        }
        return new Converter(field, getName(field), decodeObj);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if (value == null) {
            return null;
        }

        List values = new ArrayList();

        EnumSet s = (EnumSet) value;
        Object[] array = s.toArray();
        Class type = field.getType();
        for (Object anArray : array) {
            values.add(Enum.valueOf(type, String.valueOf(anArray)));
        }

        return new Converter(field, getName(field), values);
    }

}
