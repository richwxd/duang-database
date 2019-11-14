package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * @author Laotang
 */
public class TimestampConverter extends TypeConverter {

    public TimestampConverter() {
        super(Timestamp.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (null == value) {
            return null;
        }

        return new Converter(field, getName(field), new Date(((Timestamp) value).getTime()));
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {
        if (null == value) {
            return null;
        }

        Class type = field.getType();

        if(DataType.isTimestamp(type)) {
            Date date = (Date) value;
            return new Converter(field, getName(field), new Timestamp(date.getTime()));
        }

        return null;
    }
}
