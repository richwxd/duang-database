package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;

import java.lang.reflect.Field;
import java.net.URI;


/**
 * URI
 * @author Laotang
 */
public class URIConverter extends TypeConverter {

    public URIConverter() {
        this(URI.class);
    }

    protected URIConverter(final Class clazz) {
        super(clazz);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (value == null) {
            return null;
        }

        URI uri = URI.create(value.toString().replace("%46", "."));

        return new Converter(field, getName(field), uri);

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }

        String uriString = value.toString().replace(".", "%46");

        return new Converter(field, getName(field), uriString);
    }
}
