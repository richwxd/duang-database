package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;

import java.lang.reflect.Field;
import java.util.UUID;


/**
 * UUID
 *
 * provided by http://code.google.com/p/morphia/issues/detail?id=320
 *
 * @author Laotang
 */
public class UUIDConverter extends TypeConverter {

    /**
     * Creates the Converter.
     */
    public UUIDConverter() {
        super(UUID.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (null == value) {
            return null;
        }

        return new Converter(field, getName(field), UUID.fromString((String) value));
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {
        if (null == value) {
            return null;
        }
        return new Converter(field, getName(field), value.toString());
    }
}
