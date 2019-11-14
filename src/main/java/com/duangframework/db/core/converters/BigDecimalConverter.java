package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import org.bson.types.Decimal128;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *  BigDecimal
 * @author Laotang
 */
public class BigDecimalConverter extends TypeConverter {

    /**
     * Creates the Converter.
     */
    public BigDecimalConverter() {
        super(BigDecimal.class);
    }


    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }
        BigDecimal bigDecimal = null;
        if (value instanceof BigDecimal) {
            bigDecimal = (BigDecimal) value;
        }

        if (value instanceof Decimal128) {
            bigDecimal = ((Decimal128) value).bigDecimalValue();
        }

        if (value instanceof BigInteger) {
            bigDecimal = new BigDecimal(((BigInteger) value));
        }

        if (value instanceof Double) {
            bigDecimal = new BigDecimal(((Double) value));
        }

        if (value instanceof Long) {
            bigDecimal = new BigDecimal(((Long) value));
        }

        if (value instanceof Number) {
            bigDecimal = new BigDecimal(((Number) value).doubleValue());
        }

        if (value instanceof String) {
            bigDecimal = new BigDecimal(value.toString());
        }

        return new Converter(field, getName(field), bigDecimal);
    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return new Converter(field, getName(field), new Decimal128((BigDecimal) value));
        }

        return null;
    }

}
