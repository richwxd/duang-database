package com.duangframework.db.core.converters;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;


/**
 * Byte
 *
 * @author Laotang
 */
public class ByteConverter extends TypeConverter {

    public ByteConverter() {
        super(byte.class, Byte.class, byte[].class, Byte[].class);
    }


    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }
        Object byteObject = null;
        if (value.getClass().equals(field.getType())) {
            byteObject =  value;
        }

        if (value instanceof Number) {
            byteObject = ((Number) value).byteValue();
        }

        if (field.getType().isArray() && value.getClass().equals(byte[].class)) {
            byteObject =  convertToWrapperArray((byte[]) value);
        }

        if (value instanceof String) {
            byteObject =  Byte.parseByte(value.toString());
        }

        return new Converter(field, getName(field), byteObject);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {
        Class<?> type = field.getType();

        if ((value instanceof Byte[]) || (value instanceof byte[])) {
            return new Converter(field, getName(field), convertToPrimitiveArray((Byte[]) value));
        } else {
            return new Converter(field, getName(field), (byte)value);
        }
    }

    private Object convertToPrimitiveArray(final Byte[] values) {
        final int length = values.length;
        final Object array = Array.newInstance(byte.class, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, values[i]);
        }
        return array;
    }

    private Object convertToWrapperArray(final byte[] values) {
        final int length = values.length;
        final Object array = Array.newInstance(Byte.class, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, values[i]);
        }
        return array;
    }
}
